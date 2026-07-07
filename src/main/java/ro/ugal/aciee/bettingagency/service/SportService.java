package ro.ugal.aciee.bettingagency.service;

import ro.ugal.aciee.bettingagency.dao.SportDAO;
import ro.ugal.aciee.bettingagency.model.Sport;

import java.sql.SQLException;
import java.util.List;

public class SportService {
    private final SportDAO sportDAO = new SportDAO();

    public Sport save(String sportName) throws SQLException {
        if (sportName.isBlank()) {
            throw new IllegalArgumentException("Sport name is empty");
        }
        if (sportDAO.getBySportName(sportName) != null) {
            throw new IllegalArgumentException("This sport already exist");
        }
        Sport sport = new Sport();
        sport.setSportName(sportName);
        return sportDAO.save(sport);
    }

    public List<Sport> getAll() throws SQLException {
        List<Sport> sportList = sportDAO.getAll();
        if (sportList.isEmpty()) {
            throw new IllegalArgumentException("Sport list is empty");
        }
        return sportList;
    }

    public Sport getById(int sportId) throws SQLException {
        if (sportId <= 0) {
            throw new IllegalArgumentException("Incorrect sport id");
        }
        Sport sport = sportDAO.getById(sportId);
        if (sport == null) {
            throw new IllegalArgumentException("Sport not found");
        }
        return sport;
    }

    public boolean updateSportName(String sportName, int sportId) throws SQLException {
        if (sportId <= 0) {
            throw new IllegalArgumentException("Incorrect sport id");
        }
        if (sportName.isBlank()) {
            throw new IllegalArgumentException("Sport name is empty");
        }
        Sport sport = sportDAO.getById(sportId);
        if (sport == null) {
            throw new IllegalArgumentException("Sport not found");
        }
        return sportDAO.update(sportName, sportId);
    }

    public boolean delete(int sportId) throws SQLException {
        if (sportId <= 0) {
            throw new IllegalArgumentException("Incorrect sport id");
        }
        if (sportDAO.getById(sportId) == null) {
            throw new IllegalArgumentException("Sport not found");
        }
        return sportDAO.delete(sportId);
    }


}
