package model.dao;

import database.DB;
import model.dao.impl.MusicaDaoJDBC;
import model.dao.impl.PlaylistDaoJDBC;
import model.dao.impl.ReviewsDaoJDBC;
import model.dao.impl.UsuarioDaoJDBC;
import model.dao.interfaces.MusicaDAO;
import model.dao.interfaces.PlaylistDAO;
import model.dao.interfaces.ReviewDAO;
import model.dao.interfaces.UsuarioDAO;

public class DaoFactory {
    public static UsuarioDAO createuUsuarioDAO() {
        return new UsuarioDaoJDBC(DB.getConnection());
    }

    public static MusicaDAO createMusicaDAO() {
        return new MusicaDaoJDBC(DB.getConnection());
    }

    public static PlaylistDAO createPlaylistDAO() {
        return new PlaylistDaoJDBC(DB.getConnection());
    }

    public static ReviewDAO createReviewDAO() {
        return new ReviewsDaoJDBC(DB.getConnection());
    }
}