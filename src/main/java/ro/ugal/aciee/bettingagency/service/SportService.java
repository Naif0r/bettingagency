package ro.ugal.aciee.bettingagency.service;

import ro.ugal.aciee.bettingagency.dao.SportDAO;
import ro.ugal.aciee.bettingagency.model.Sport;

import java.sql.SQLException;
import java.util.List;

public class SportService {
    private final SportDAO sportDAO = new SportDAO();

    public Sport save(String sportName) throws SQLException {
        sportName = sportName.trim();

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

    public Sport importer(Sport sport) {
        try {
            return sportDAO.importer(sport);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<Sport> getAll() throws SQLException {
        return sportDAO.getAll();
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

    public boolean updateSportName(String newSportName, int sportId) throws SQLException {
        Sport sport = getById(sportId);
        newSportName = newSportName.trim();

        if (newSportName.isBlank()) {
            throw new IllegalArgumentException("Sport name is empty");
        }

        if (sport.getSportName().equals(newSportName)) {
            throw new IllegalArgumentException("The new sport name must be different from the current one");
        }

        if (sportDAO.getBySportName(newSportName) != null) {
            throw new IllegalArgumentException("Сannot use a name that already exists in the database");
        }

        return sportDAO.update(newSportName, sportId);
    }

    public boolean delete(int sportId) throws SQLException {
        getById(sportId);
        return sportDAO.delete(sportId);
    }
}
