package tech.shann.entity.enums;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by shann on 17/7/3.
 */
public class IntEnumTypeHandler extends BaseTypeHandler<IntEnum> {

    private Class<IntEnum> type;
    public IntEnumTypeHandler(Class<IntEnum> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, IntEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getValue());
    }
    @Override
    public IntEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int i = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return getValuedEnum(i);
        }
    }
    @Override
    public IntEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int i = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return getValuedEnum(i);
        }
    }
    @Override
    public IntEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int i = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return getValuedEnum(i);
        }
    }
    private IntEnum getValuedEnum(int value) {
        IntEnum[] objs = type.getEnumConstants();
        for(IntEnum em:objs){
            if(em.getValue()==value){
                return  em;
            }
        }
        throw new IllegalArgumentException(
                "Cannot convert " + value + " to " + type.getSimpleName() + " by value.");
    }
}
