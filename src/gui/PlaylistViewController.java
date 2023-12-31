package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import database.DbException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.dao.DaoFactory;
import model.dao.interfaces.MusicaDAO;
import model.dao.interfaces.PlaylistDAO;
import model.entities.Musica;
import model.entities.Playlist;
import model.entities.Usuario;

public class PlaylistViewController implements Initializable {
    @FXML
    private TableView<Musica> tablePlaylistMusicas;
    @FXML
    private TableView<Musica> tableMusicas;
    @FXML
    private TableColumn<Musica, String> columnMusicTitulo;
    @FXML
    private TableColumn<Musica, String> columnMusicArtista;
    @FXML
    private TableColumn<Musica, String> columnTitulo;
    @FXML
    private TableColumn<Musica, String> columnArtista;
    @FXML
    private TableColumn<Musica, String> columnEstilo;
    @FXML
    private TableColumn<Musica, Integer> columnAnoLanc;
    @FXML
    private Text txtPlaylistName;
    @FXML
    private Text txtUserName;
    @FXML
    private TextField txtPesquisar;
    @FXML
    private Button buttonAddSong;
    @FXML
    private Button buttonRemoverMusica;

    private Playlist playlist;
    private Usuario usuario;

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private PlaylistDAO playlistDAO = DaoFactory.createPlaylistDAO();
    private MusicaDAO musicaDAO = DaoFactory.createMusicaDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnMusicTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        columnMusicArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        columnTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        columnArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        columnEstilo.setCellValueFactory(new PropertyValueFactory<>("estilo"));
        columnAnoLanc.setCellValueFactory(new PropertyValueFactory<>("anoLanc"));
        configureTableColumns();
        List<Musica> musicas = musicaDAO.findAll();
        tableMusicas.getItems().addAll(musicas);
    }

    public void configureTableColumns() {

        if (playlist != null) {
            txtPlaylistName.setText(playlist.getNome());
            txtUserName.setText(usuario.getUsername());
            List<Musica> musicas = playlistDAO.obterMusicasDaPlaylist(playlist);
            tablePlaylistMusicas.getItems().addAll(musicas);
        }
    }

    @FXML
    private void adicionarMusica() {
        atualizarTabelaPlaylistUsuario();
    }

    private void atualizarTabelaPlaylistUsuario() {
        if (playlist != null) {
            ObservableList<Musica> musicasDaPlaylist = playlistDAO.getMusicasDaPlaylist(playlist);
            tablePlaylistMusicas.setItems(musicasDaPlaylist);
        }
    }

    @FXML
    public void searchButton() {
        String nomeMusica = txtPesquisar.getText();
        List<Musica> musicasEncontradas = musicaDAO.findByMusica(nomeMusica);
        exibirMusicasEncontradas(musicasEncontradas);
    }

    private void exibirMusicasEncontradas(List<Musica> musicas) {
        tableMusicas.getItems().clear();
        tableMusicas.getItems().addAll(musicas);
    }

    @FXML
    private void onHandleAdicionarMusica() {
        Musica musicaSelecionada = tableMusicas.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null && playlist != null) {
            boolean verificar = playlistDAO.verificarMusica(musicaSelecionada, playlist);
            if (verificar != true) {
                playlist = playlistDAO.salvarMusicaPlaylist(playlist, musicaSelecionada);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Aviso");
                alert.setHeaderText(null);
                alert.setContentText("Musica ja adicionada!");
                alert.showAndWait();
            }
        }
        atualizarTabelaPlaylistUsuario();
    }

    @FXML
    private void onHandleExcluirMusica() {
        Musica musicaSelecionada = tablePlaylistMusicas.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null && playlist != null) {
            excluirMusicaDaPlaylist(musicaSelecionada);
        }
    }

    private void excluirMusicaDaPlaylist(Musica musica) {
        int playlistId = playlist.getId();
        int musicaId = musica.getId();
        try {
            playlistDAO.deletarMusicaDaPlaylist(playlistId, musicaId);
            atualizarTabelaPlaylistUsuario();
        } catch (DbException e) {
            throw new DbException(e.getMessage());
        }
    }
}
