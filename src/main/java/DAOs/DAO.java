package DAOs;

import Helper.SystemException;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, ID> {


    void add(T t) throws SQLException, SystemException;

    void update(T t, ID id) throws SQLException, SystemException;

    void drop(ID id) throws SQLException;

    List<T> getAll() throws SQLException;

    T getOne(ID id) throws SQLException, SystemException;
}
