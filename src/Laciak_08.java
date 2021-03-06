import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

class SQLiteHelper implements SQLiteHelperInterface {
    @Override
    public String createTable(Object o) {
        String createString = "";
        if (o != null) {
            Class myClass = o.getClass();
            String className = myClass.getSimpleName();
            Map<String, String> allowedFields = getPublicFieldsString(myClass);
            createString = buildCreateString(className, allowedFields);
        }
        return createString;
    }

    @Override
    public String insert(Object o) {
        String insertString = "";
        if (o != null) {
            Class myClass = o.getClass();
            String className = myClass.getSimpleName();
            Map<String, String> allowedFields = null;
            try {
                allowedFields = getPublicFieldsValuesString(myClass,o);
            } catch (IllegalAccessException | InstantiationException ignored) {
            }
            insertString = buildInsertString(className, allowedFields);
        }
        return insertString;
    }

    private Map<String, String> getPublicFieldsString(Class c) {
        Field[] allFields = c.getDeclaredFields();
        Map<String, String> fields = new HashMap<>();
        for (Field f : allFields) {
            if (f.getModifiers() == Modifier.PUBLIC) {
                if (isTypeAllowed(f.getType())) {
                    fields.put(f.getName(), f.getType().getSimpleName());
                }

            }
        }
        return fields;
    }

    private Map<String, String> getPublicFieldsValuesString(Class c, Object o) throws IllegalAccessException, InstantiationException {
        Field[] allFields = c.getDeclaredFields();
        Map<String, String> fields = new HashMap<>();
        for (Field f : allFields) {
            if (f.getModifiers() == Modifier.PUBLIC) {
                if (isTypeAllowed(f.getType())) {
                    Object value = f.get(o);
                    if(value == null){
                        value = "0";
                    }
                    value = value.toString();
                        if (f.getType().getSimpleName().equals("boolean") || f.getType().getSimpleName().equals("Boolean")) {
                            if (value.equals("true")) {
                                value = "1";
                            } else if (value.equals("false")) {
                                value = "0";

                            }
                        } else if (f.getType().getSimpleName().equals("String")) {
                            value = "'" + value + "'";
                        }
                    fields.put(f.getName(), (String)value);

                }
            }
        }
        return fields;
    }

    private boolean isTypeAllowed(Class type) {
        Class[] allowedTypes = {
                int.class, long.class, Integer.class, Long.class,
                float.class, double.class, Float.class, Double.class,
                String.class,
                boolean.class, Boolean.class
        };
        for (Class c : allowedTypes) {
            if (c == type) {
                return true;
            }
        }
        return false;
    }

    private String buildCreateString(String className, Map<String, String> fields) {
        String createString = "CREATE TABLE IF NOT EXISTS ";
        createString += className + "(";
        int item = 0;
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            createString += entry.getKey() + " " + convertJavaToSQL(entry.getValue());
            if (item < fields.size() - 1) {
                createString += ", ";
            }
            item++;
        }
        createString += ");";
        return createString;
    }

    private String convertJavaToSQL(String type) {
        String[] ints = {"int", "long", "Integer", "Long"};
        Arrays.sort(ints);
        String[] reals = {"float", "double", "Float", "Double"};
        Arrays.sort(reals);
        String[] strings = {"String"};
        String[] bools = {"boolean", "Boolean"};
        Arrays.sort(bools);
        if (Arrays.binarySearch(ints, type) >= 0) {
            return "INTEGER";
        } else if (Arrays.binarySearch(reals, type) >= 0) {
            return "REAL";
        } else if (strings[0].equals(type)) {
            return "TEXT";
        } else {
            return "INTEGER";
        }
    }

    private String buildInsertString(String className, Map<String, String> values) {
        String insertString = "INSERT INTO ";
        String types = "(";
        String value = "(";
        insertString += className + " ";
        int step = 0;
        for (Map.Entry entry : values.entrySet()) {
            types += entry.getKey();
            value += entry.getValue();
            if (step < values.size() - 1) {
                types += ", ";
                value += ", ";
            } else {
                types += ")";
                value += ");";
            }
            step++;
        }
        insertString += types;
        insertString += " VALUES ";
        insertString += value;
        return insertString;
    }
}