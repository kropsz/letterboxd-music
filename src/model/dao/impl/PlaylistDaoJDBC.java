package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import database.DB;
import database.DbException;
import model.dao.interfaces.PlaylistDAO;
import model.entities.Playlist;

public class PlaylistDaoJDBC implements PlaylistDAO{
    private Connection conn;

    public PlaylistDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Playlist findById(Integer id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public void salvarMusicaPlaylist(Integer playlistID, Integer musicaID) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("""
                "INSERT INTO musicas_playlist (PlaylistID, MusicaID) VALUES (?, ?)"
                    """);
            st.setInt(1, playlistID);
            st.setInt(2, musicaID);    
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
       
    }

    @Override
    public List<Playlist> findAll() {
      
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public void create(Playlist playlist) {
       PreparedStatement st = null;
       try {
            st = conn.prepareStatement("""
                INSERT INTO playlists(Nome, UsuarioID, Descricao)
                VALUES (?, ?, ?)
            """,Statement.RETURN_GENERATED_KEYS);
            st.setString(1, playlist.getNome());
            st.setInt(2, playlist.getUser().getId());
            st.setString(3, playlist.getDesc());

            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    playlist.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else{
                throw new DbException("No rows affected");
            }
       } catch (SQLException e) {
        throw new DbException(e.getMessage());
       }
       finally{
        DB.closeStatement(st);
       }
    }

    @Override
    public void update(Playlist playlist) {
        
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Integer id) {
       
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}