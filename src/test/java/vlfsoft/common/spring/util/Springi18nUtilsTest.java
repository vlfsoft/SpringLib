package vlfsoft.common.spring.util;

import org.junit.Test;

import org.jetbrains.annotations.NotNull;

public class Springi18nUtilsTest {

    static class A {
        static class B {
            static class C {
                static class D {

                }

            }
        }

    }

    private static <E extends Enum> String getNestedClassName(final @NotNull E aEnumType) {
        Class clazz = aEnumType.getClass();
        Class enclosingClazz = clazz.getEnclosingClass();
        String nestedClassName = (enclosingClazz != null ? enclosingClazz.getSimpleName() + "." : "") + clazz.getSimpleName();
        enclosingClazz = enclosingClazz != null ? enclosingClazz.getEnclosingClass() : null;
        nestedClassName = (enclosingClazz != null ? enclosingClazz.getSimpleName() + "." : "") + nestedClassName;
        return nestedClassName;
    }

    private static String getNestedClassNameR(final @NotNull Class aClass) {
        Class enclosingClass = aClass.getEnclosingClass();
        return enclosingClass == null ? aClass.getSimpleName() : getNestedClassNameR(enclosingClass) + "." + aClass.getSimpleName();
    }

    @Test
    public void doTest() {
        A.B.C.D d = new A.B.C.D();

        System.out.println(getNestedClassNameR(Springi18nUtilsTest.class));
        System.out.println(getNestedClassNameR(A.class));
        System.out.println(getNestedClassNameR(A.B.class));
        System.out.println(getNestedClassNameR(A.B.C.class));
        System.out.println(getNestedClassNameR(A.B.C.D.class));

        System.out.println(d.getClass().getSimpleName());
    }
}
