package com.streamingservicebackend.model.streamingservice;

import com.streamingservicebackend.database.DatabaseHandler;
import com.streamingservicebackend.database.ISqlHandler;
import com.streamingservicebackend.model.Dao;
import com.streamingservicebackend.util.BooleanUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StreamingServiceDAO implements Dao<StreamingService> {

    ISqlHandler _sqlHandler;

    public StreamingServiceDAO(ISqlHandler sqlHandler) {
        this._sqlHandler = sqlHandler;
    }

    @Override
    public Optional<StreamingService> get(String id) {
        StreamingService s = null;

        try {
            String query = "SELECT * FROM " + DatabaseHandler.TABLE_SERVICE + " WHERE " + DatabaseHandler.COLUMN_SERVICE_ID + " = '" + id + "'";
            ResultSet rs = _sqlHandler.executeQuery(query);

            if (rs.next()) {
                s = getFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        if (s == null) {
            return Optional.empty();
        }
        return Optional.of(s);
    }

    @Override
    public List<StreamingService> getAll() {
        List<StreamingService> services = new ArrayList<>();
        ResultSet rs;

        try {
            String query = "SELECT * FROM " + DatabaseHandler.TABLE_SERVICE;
            rs = _sqlHandler.executeQuery(query);
            while (rs != null && rs.next()) {
                services.add(getFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return services;
        }

        return services;
    }

    @Override
    public boolean add(StreamingService s) {
        if (s == null) {
            return false;
        }

        String sql = String.format("INSERT INTO " + DatabaseHandler.TABLE_SERVICE + " VALUES ('%s', '%s', %.2f, %d, %d)", s.getId(), s.getName(), s.getMonthlySubscriptionPrice(), s.getFreeTrialLength(), BooleanUtil.toInt(s.getShowsAds()));
        return _sqlHandler.executeSql(sql);
    }

    @Override
    public boolean update(StreamingService s) {
        if (s == null) {
            return false;
        }

        String template =
                "UPDATE " + DatabaseHandler.TABLE_SERVICE + " SET " +
                        DatabaseHandler.COLUMN_SERVICE_NAME + " = '%s' SET " +
                        DatabaseHandler.COLUMN_SERVICE_SUBSCRIPTION_PRICE + " = %.2f SET " +
                        DatabaseHandler.COLUMN_SERVICE_TRIAL_PERIOD_DAYS + " = %d SET " +
                        DatabaseHandler.COLUMN_SERVICE_SHOWS_ADS + " = %d WHERE " +
                        DatabaseHandler.COLUMN_SERVICE_ID + " = '%s'";

        String sql = String.format(template, s.getName(), s.getMonthlySubscriptionPrice(), s.getFreeTrialLength(), BooleanUtil.toInt(s.getShowsAds()), s.getId());
        return _sqlHandler.executeSql(sql);
    }

    @Override
    public boolean delete(StreamingService s) {
        if (s == null) {
            return false;
        }
        String template = "DELETE FROM " + DatabaseHandler.TABLE_SERVICE + " WHERE " + DatabaseHandler.COLUMN_SERVICE_ID + " = '%s'";
        String sql = String.format(template, s.getId());
        return _sqlHandler.executeSql(sql);
    }

    private StreamingService getFromResultSet(ResultSet rs) throws SQLException {
        String id = rs.getString(1);
        String name = rs.getString(2);
        double price = rs.getDouble(3);
        int trialLength = rs.getInt(4);
        boolean showsAds = BooleanUtil.toBool(rs.getInt(5));
        return new StreamingService(id, name, price, trialLength, showsAds);
    }

}
