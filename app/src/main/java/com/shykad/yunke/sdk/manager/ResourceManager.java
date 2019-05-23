package com.shykad.yunke.sdk.manager;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Create by wanghong.he on 2019/3/22.
 * description：反射资源
 */
public class ResourceManager {

    public static int getIdByName(Context context, String className, String resName) {
        String packageName = context.getPackageName();
        int id = 0;
        try {
            Class r = Class.forName(packageName + ".R");
            Class[] classes = r.getClasses();
            Class desireClass = null;
            for (Class cls : classes) {
                if (cls.getName().split("\\$")[1].equals(className)) {
                    desireClass = cls;
                    break;
                }
            }
            if (desireClass != null) {
                id = desireClass.getField(resName).getInt(desireClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int getResId(Context context, String paramString1, String paramString2) {
        return context.getResources().getIdentifier(paramString2, paramString1, context.getPackageName());
    }

    public static int getLayoutId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "layout", paramContext.getPackageName());
    }

    public static int getStringId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString, "string", paramContext.getPackageName());
    }

    public static int getDrawableId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,"drawable", paramContext.getPackageName());
    }

    public static int getStyleId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,"style", paramContext.getPackageName());
    }

    public static int getId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,"id", paramContext.getPackageName());
    }

    public static int getColorId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,"color", paramContext.getPackageName());
    }

    public static int getArrayId(Context paramContext, String paramString) {
        return paramContext.getResources().getIdentifier(paramString,"array", paramContext.getPackageName());
    }

    public static int[] getStyleableIntArray(Context context, String name) {
        try {
            //.与$ difference,$表示R的子类
            Field[] fields = Class.forName(context.getPackageName() + ".R$styleable").getFields();
            for (Field field : fields) {
                if (field.getName().equals(name)) {
                    int[] ret = (int[]) field.get(null);
                    return ret;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 遍历R类得到styleable数组资源下的子资源，1.先找到R类下的styleable子类，2.遍历styleable类获得字段值
     * @param context
     * @param styleableName
     * @param styleableFieldName
     * @return
     */
    public static int getStyleableFieldId(Context context, String styleableName, String styleableFieldName) {
        String className = context.getPackageName() + ".R";
        String type = "styleable";
        String name = styleableName + "_" + styleableFieldName;
        try {
            Class<?> cla = Class.forName(className);
            for (Class<?> childClass : cla.getClasses()) {
                String simpleName = childClass.getSimpleName();
                if (simpleName.equals(type)) {
                    for (Field field : childClass.getFields()) {
                        String fieldName = field.getName();
                        if (fieldName.equals(name)) {
                            return (int) field.get(null);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }
}
