import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.*;

/**
 * Created by ldar on 2016-02-25.
 */
class SQLiteHelperExt implements SQLiteHelperExtInterface {

    @Override
    public List<Object> select(Connection connection, String tableName) {
        Statement statement = prepareStatement(connection);
        ResultSet rs = extractResult(statement,tableName);
        return convertResultSetToListObject(rs,tableName);
    }

    private List<Object> convertResultSetToListObject(ResultSet rs, String className){
        List<Object> objects = new ArrayList<>();
        Class myClass = null;
        try {
           myClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, String> publicFieldsString = getPublicFieldsString(myClass);
        Set<String> fieldNames = publicFieldsString.keySet();

        try {
            while(rs.next()) {
                Object ob = myClass.newInstance();
                ResultSetMetaData rsmd = rs.getMetaData();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (fieldNames.contains(rsmd.getColumnLabel(i))) {
                        Field field = ob.getClass().getField(rsmd.getColumnLabel(i));
                        setField(field,rs.getObject(i),ob);
                    }
                }
                objects.add(ob);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return objects;
    }

    private void setField(Field field, Object valueObj, Object toSet) {
        String fieldName = field.getType().getSimpleName();
        try {
            if ((fieldName.equals("boolean") || fieldName.equals("Boolean")) && valueObj.getClass().getSimpleName().equals("Integer")){
                int value = (Integer) valueObj;
                Boolean b;
                if(value == 0){
                    b = false;
                }
                else{
                    b = true;
                }
                if(fieldName.equals("Boolean")){
                    field.set(toSet,b);
                }
                else {
                    field.set(toSet,b.booleanValue());
                }
            }
             else if (fieldName.equals("float") || fieldName.equals("Float")) {
                Double dbl = (Double) valueObj;
                field.set(toSet, dbl.floatValue());

            } else if (fieldName.equals("boolean")) {
                Boolean bl = (Boolean) valueObj;
                field.set(toSet, bl.booleanValue());
            } else if (fieldName.equals("int")|| fieldName.equals("Integer")){
                Integer dbl = (Integer) valueObj;
                field.set(toSet, dbl.intValue());
            } else if (fieldName.equals("long") || fieldName.equals("Long")){
                Integer dbl = (Integer) valueObj;
                field.set(toSet, dbl.longValue());
            }
            else {
                field.set(toSet,valueObj);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private ResultSet extractResult(Statement statement, String className){
        ResultSet rs = null;
        try {
            rs = statement.executeQuery("SELECT * FROM "+className);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private Statement prepareStatement(Connection connection){
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

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
