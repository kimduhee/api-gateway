package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.framework.gw.common.utils.BeanUtils;
import com.framework.gw.entity.InterfaceInfoEntity;
import com.framework.gw.service.InterfaceService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.concurrent.Future;

@Slf4j
public abstract class PropertyNamingStrategies implements Serializable {
    private static final long serialVersionUID = 2L;
    public static final PropertyNamingStrategy LOWER_CAMEL_CASE;
    public static final PropertyNamingStrategy UPPER_CAMEL_CASE;
    public static final PropertyNamingStrategy SNAKE_CASE;
    public static final PropertyNamingStrategy UPPER_SNAKE_CASE;
    public static final PropertyNamingStrategy LOWER_CASE;
    public static final PropertyNamingStrategy KEBAB_CASE;
    public static final PropertyNamingStrategy LOWER_DOT_CASE;
    public static final PropertyNamingStrategy CONVERT_CASE;

    public PropertyNamingStrategies() {
    }

    static {
        LOWER_CAMEL_CASE = LowerCamelCaseStrategy.INSTANCE;
        UPPER_CAMEL_CASE = UpperCamelCaseStrategy.INSTANCE;
        SNAKE_CASE = SnakeCaseStrategy.INSTANCE;
        UPPER_SNAKE_CASE = UpperSnakeCaseStrategy.INSTANCE;
        LOWER_CASE = LowerCaseStrategy.INSTANCE;
        KEBAB_CASE = KebabCaseStrategy.INSTANCE;
        LOWER_DOT_CASE = LowerDotCaseStrategy.INSTANCE;
        CONVERT_CASE = ConvertCaseStrategy.INSTANCE;
    }

    public abstract static class NamingBase extends PropertyNamingStrategy {
        private static final long serialVersionUID = 2L;

        public NamingBase() {
        }

        public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
            return this.translate(defaultName);
        }

        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return this.translate(defaultName);
        }

        public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
            return this.translate(defaultName);
        }

        public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam, String defaultName) {
            return this.translate(defaultName);
        }

        public abstract String translate(String var1);

        protected String translateLowerCaseWithSeparator(String input, char separator) {
            if (input != null && !input.isEmpty()) {
                int length = input.length();
                StringBuilder result = new StringBuilder(length + (length >> 1));
                int upperCount = 0;

                for(int i = 0; i < length; ++i) {
                    char ch = input.charAt(i);
                    char lc = Character.toLowerCase(ch);
                    if (lc == ch) {
                        if (upperCount > 1) {
                            result.insert(result.length() - 1, separator);
                        }

                        upperCount = 0;
                    } else {
                        if (upperCount == 0 && i > 0) {
                            result.append(separator);
                        }

                        ++upperCount;
                    }

                    result.append(lc);
                }

                return result.toString();
            } else {
                return input;
            }
        }
    }

    public static class SnakeCaseStrategy extends NamingBase {
        private static final long serialVersionUID = 2L;
        public static final PropertyNamingStrategies.SnakeCaseStrategy INSTANCE = new PropertyNamingStrategies.SnakeCaseStrategy();

        public SnakeCaseStrategy() {
        }

        public String translate(String input) {
            if (input == null) {
                return input;
            } else {
                int length = input.length();
                StringBuilder result = new StringBuilder(length * 2);
                int resultLength = 0;
                boolean wasPrevTranslated = false;

                for(int i = 0; i < length; ++i) {
                    char c = input.charAt(i);
                    if (i > 0 || c != '_') {
                        if (Character.isUpperCase(c)) {
                            if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
                                result.append('_');
                                ++resultLength;
                            }

                            c = Character.toLowerCase(c);
                            wasPrevTranslated = true;
                        } else {
                            wasPrevTranslated = false;
                        }

                        result.append(c);
                        ++resultLength;
                    }
                }

                return resultLength > 0 ? result.toString() : input;
            }
        }
    }

    public static class UpperSnakeCaseStrategy extends SnakeCaseStrategy {
        private static final long serialVersionUID = 2L;
        public static final UpperSnakeCaseStrategy INSTANCE = new UpperSnakeCaseStrategy();

        public UpperSnakeCaseStrategy() {
        }

        public String translate(String input) {
            String output = super.translate(input);
            return output == null ? null : output.toUpperCase();
        }
    }

    public static class LowerCamelCaseStrategy extends NamingBase {
        private static final long serialVersionUID = 2L;
        public static final LowerCamelCaseStrategy INSTANCE = new LowerCamelCaseStrategy();

        public LowerCamelCaseStrategy() {
        }

        public String translate(String input) {
            return input;
        }
    }

    public static class UpperCamelCaseStrategy extends NamingBase {
        private static final long serialVersionUID = 2L;
        public static final PropertyNamingStrategies.UpperCamelCaseStrategy INSTANCE = new PropertyNamingStrategies.UpperCamelCaseStrategy();

        public UpperCamelCaseStrategy() {
        }

        public String translate(String input) {
            if (input != null && !input.isEmpty()) {
                char c = input.charAt(0);
                char uc = Character.toUpperCase(c);
                if (c == uc) {
                    return input;
                } else {
                    StringBuilder sb = new StringBuilder(input);
                    sb.setCharAt(0, uc);
                    return sb.toString();
                }
            } else {
                return input;
            }
        }
    }

    public static class LowerCaseStrategy extends NamingBase {
        private static final long serialVersionUID = 2L;
        public static final PropertyNamingStrategies.LowerCaseStrategy INSTANCE = new PropertyNamingStrategies.LowerCaseStrategy();

        public LowerCaseStrategy() {
        }

        public String translate(String input) {
            return input != null && !input.isEmpty() ? input.toLowerCase() : input;
        }
    }

    public static class KebabCaseStrategy extends NamingBase {
        private static final long serialVersionUID = 2L;
        public static final PropertyNamingStrategies.KebabCaseStrategy INSTANCE = new PropertyNamingStrategies.KebabCaseStrategy();

        public KebabCaseStrategy() {
        }

        public String translate(String input) {
            return this.translateLowerCaseWithSeparator(input, '-');
        }
    }

    public static class LowerDotCaseStrategy extends NamingBase {
        private static final long serialVersionUID = 2L;
        public static final PropertyNamingStrategies.LowerDotCaseStrategy INSTANCE = new PropertyNamingStrategies.LowerDotCaseStrategy();

        public LowerDotCaseStrategy() {
        }

        public String translate(String input) {
            return this.translateLowerCaseWithSeparator(input, '.');
        }
    }

    public static class ConvertCaseStrategy extends NamingBase {
        private static final long serialVersionUID = 2L;
        public static final ConvertCaseStrategy INSTANCE = new ConvertCaseStrategy();

        @Override
        public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {

            InterfaceService interfaceService = (InterfaceService) BeanUtils.getBean("interfaceService");

            //변경할 필드명
            String convKeyName = defaultName;

            try {
                /**
                 * Db에서 패키지명을 키로 저장되어 있는 변환정보를 가져옴
                 * package => method.getDeclaringClass().getName()
                 * 필드명 => method.getDeclaringClass().getDeclaredField(defaultName).getName()
                 *
                 * 예)
                 * 패키지(KEY) com.framework.template.web
                 * 필드명: cateId => 변경할 필드명: 카테고리ID
                 * 필드명: cateNm => 변경할 필드명: 카테고리명
                 */
                Mono<InterfaceInfoEntity> dbData = interfaceService.interfaceList(method.getDeclaringClass().getName(), method.getDeclaringClass().getDeclaredField(defaultName).getName());

                //동기 처리하여 DB 데이터 값을 return 한다.
                Future<InterfaceInfoEntity> future = dbData.toFuture();
                InterfaceInfoEntity interfaceInfo = future.get();
                if(interfaceInfo != null) {
                    convKeyName = interfaceInfo.getNewName();
                }
            } catch (Exception e) {
                log.error("conversion error");
            }
            return this.translate(convKeyName);
        }
        public ConvertCaseStrategy() {
        }

        public String translate(String input) {
            return input;
        }
    }
}
