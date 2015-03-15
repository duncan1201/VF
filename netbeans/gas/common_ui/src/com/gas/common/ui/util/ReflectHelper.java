/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class ReflectHelper {

    public static Object invoke(Object o, String methodName) {
        return invoke(o, methodName, null, null);
    }

    public static Object invoke(Object o, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Method method = getMethodQuietly(o.getClass(), methodName, parameterTypes);
        return invoke(o, method, args);
    }

    public static Object invoke(Object o, Method method, Object... args) {
        Object ret = null;
        if (method == null) {
            return ret;
        }
        try {
            if (args != null && args.length > 0) {                
                ret = method.invoke(o, args);
            } else {
                ret = method.invoke(o);
            }
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public List<String> getDeclaredFieldNames(Class _class) {
        List<String> ret = new ArrayList<String>();
        Field[] fields = _class.getDeclaredFields();
        for (Field f : fields) {
            ret.add(f.getName());
        }
        return ret;
    }
    
    public static Method getDeclaredMethod(Class _clazz, String methodName, Class<?>... paramTypes){
        Method ret = null;
        try {
            if(paramTypes != null && paramTypes.length > 0){
                ret = _clazz.getDeclaredMethod(methodName, paramTypes);
            }else{
                ret = _clazz.getDeclaredMethod(methodName);
            }
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }

    public static List<Field> getDeclaredFields(Class _clazz) {
        return getDeclaredFields(_clazz, null, false);
    }

    public static Method getAccessor(Field field) {
        String nameField = field.getName();
        Class classField = field.getType();

        Method ret = getMethodQuietly(field.getDeclaringClass(), "get" + StrUtil.uppercaseFirstLetter(nameField));
        if (ret == null && (classField.isAssignableFrom(boolean.class) || classField.isAssignableFrom(Boolean.class))) {
            ret = getMethodQuietly(field.getDeclaringClass(), "is" + StrUtil.uppercaseFirstLetter(nameField));
        }
        return ret;
    }

    public static Method getMutator(Field field) {
        String nameField = field.getName();
        Method ret = getMethodQuietly(field.getDeclaringClass(), "set" + StrUtil.uppercaseFirstLetter(nameField), field.getType());
        return ret;
    }

    public static Object getQuietly(Field field, Object obj) {
        Object ret = null;
        try {
            final int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                ret = field.get(obj);
            } else {
                Method accessor = getAccessor(field);
                if (accessor != null) {
                    ret = invoke(obj, accessor);
                }
            }

        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return ret;
        }
    }

    public static <T> T getQuietly(Field field, Object obj, Class<T> clazz) {
        T ret = null;
        try {
            Object value = field.get(obj);
            if (value != null) {
                ret = (T) value;
            }
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return ret;
        }
    }

    public static void setQuietly(Field field, Object obj, Object value) {
        try {
            final int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers)) {
                field.set(obj, value);
            } else {
                Method mutator = getMutator(field);
                if (mutator != null) {
                    invoke(obj, mutator, value);
                }
            }
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static void setQuietly(Field field, Object obj, int value) {
        try {
            field.setInt(obj, value);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static List<Field> getDeclaredFields(Class _clazz, Class _type, Boolean isStatic) {
        List<Field> ret = new ArrayList<Field>();
        Field[] fds = _clazz.getDeclaredFields();
        for (Field fd : fds) {
            Class type = fd.getType();
            if (isStatic != null) {
                int m = fd.getModifiers();
                if (Modifier.isStatic(m) != isStatic) {
                    continue;
                }
            }
            if (_type == null) {
                ret.add(fd);
            } else if (_type != null && type.toString().equals(_type.toString())) {
                ret.add(fd);
            }
        }
        return ret;
    }

    public static <T> T getStaticQuietly(Field fd, Class<T> clazz) {
        return getQuietly(fd, null, clazz);
    }

    public static Method getMethodQuietly(Class clazz, String name, Class<?>... parameterTypes) {
        Method ret = null;
        try {
            if (parameterTypes == null || parameterTypes.length == 0) {
                ret = clazz.getMethod(name, parameterTypes);
            } else {
                ret = clazz.getMethod(name, parameterTypes);
            }
        } catch (NoSuchMethodException ex) {
            System.out.print("");
        } catch (SecurityException ex) {
            System.out.print("");
        }
        return ret;
    }

    public static Object newInstance(String clazzName, ClassLoader loader) {
        Object ret = null;
        try {
            Class clazz = Class.forName(clazzName, true, loader);
            ret = clazz.newInstance();
        } catch (ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            return ret;
        }
    }

    public static boolean checkClass(String name) {
        boolean ret;
        try {
            Class.forName(name);
            ret = true;
        } catch (ClassNotFoundException ex) {
            ret = false;
        }
        return ret;
    }
}
