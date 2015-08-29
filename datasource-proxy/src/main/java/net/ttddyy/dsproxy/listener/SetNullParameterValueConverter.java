package net.ttddyy.dsproxy.listener;

import net.ttddyy.dsproxy.proxy.ParameterSetOperation;

/**
 * Convert setNull parameter operation.
 *
 * @author Tadaya Tsuyukubo
 * @since 1.4
 */
public class SetNullParameterValueConverter implements ParameterValueConverter {

    @Override
    public String getValue(ParameterSetOperation param) {

        Integer sqlType = (Integer) param.getArgs()[1];  // second arg for setNull is always int
        String sqlTypeName = ParameterValueConverter.SQL_TYPENAME_BY_CODE.get(sqlType);

        StringBuilder sb = new StringBuilder();
        sb.append("NULL");
        if (sqlTypeName != null) {
            sb.append("(" + sqlTypeName + ")");
        }
        return sb.toString();
    }


}