
import com.tuhoc.dreamtunes.data.pojo.Album
import com.tuhoc.dreamtunes.data.pojo.Favorite
import com.tuhoc.dreamtunes.data.pojo.Playlist
import com.tuhoc.dreamtunes.data.pojo.PlaylistSong
import com.tuhoc.dreamtunes.data.pojo.Singer
import com.tuhoc.dreamtunes.data.pojo.Song
import com.tuhoc.dreamtunes.data.pojo.Type
import com.tuhoc.dreamtunes.data.pojo.User
import com.tuhoc.dreamtunes.utils.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface SongApi {
    @GET(Constants.ALL_SONG_URL)
    suspend fun getSongs(): Response<List<Song>> // suspend để đánh dấu là một hàm chờ

    @GET(Constants.ALL_TYPE_URL)
    suspend fun getTypes(): Response<List<Type>>

    @GET(Constants.ALL_SINGER_URL)
    suspend fun getSingers(): Response<List<Singer>>

    @GET("${Constants.SONG_BY_TYPE_URL}{typeId}")
    suspend fun getSongsByType(@Path("typeId") typeId: Int): Response<List<Song>>

    @GET("${Constants.SONG_BY_SINGER_URL}{singerId}")
    suspend fun getSongsBySinger(@Path("singerId") singerId: Int): Response<List<Song>>

    @GET("${Constants.SONG_BY_ALBUM_URL}{albumId}")
    suspend fun getSongsByAlbum(@Path("albumId") albumId: Int): Response<List<Song>>

    @GET("${Constants.ALBUM_BY_SINGER_URL}{singerId}")
    suspend fun getAlbumsBySinger(@Path("singerId") singerId: Int): Response<List<Album>>

    @GET(Constants.ALL_USER_URL)
    suspend fun getUsers(): Response<List<User>>

    @POST(Constants.INSERT_USER_URL)
    suspend fun addUser(@Body user: User): Response<User>

    @GET(Constants.CHECK_EMAIL)
    suspend fun checkEmailExistence(@Query("email") email: String): Response<Boolean>

    @PUT("${Constants.UPDATE_USER_URL}{userId}")
    suspend fun updateUser(@Path("userId") userId: Int, @Body user: User): Response<User>

    @GET("${Constants.USER_BY_ID_URL}{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): Response<User>

    @GET("${Constants.ALL_FAVORITE_URL}{userId}")
    suspend fun getFavoriteSongs(@Path("userId") userId: Int): Response<List<Song>>

    @POST(Constants.INSERT_FAVORITE_URL)
    suspend fun addFavoriteSong(@Body favorite: Favorite): Response<Favorite>

    @DELETE("${Constants.DELETE_FAVORITE_URL}{userId}/{songId}")
    suspend fun deleteFavoriteSong(@Path("userId") userId: Int, @Path("songId") songId: Int): Response<String>

    @GET("${Constants.CHECK_FAVORITE}{userId}/{songId}")
    suspend fun checkFavoriteExistence(@Path("userId") userId: Int, @Path("songId") songId: Int): Response<Boolean>

    @POST(Constants.INSERT_PLAYLIST_URL)
    suspend fun addPlaylist(@Body playlist: Playlist): Response<Playlist>

    @GET("${Constants.PLAYLIST_BY_USER_URL}{userId}")
    suspend fun getPlaylistsByUser(@Path("userId") userId: Int): Response<List<Playlist>>

    @GET("${Constants.SONG_BY_PLAYLIST_URL}{playlistId}")
    suspend fun getSongsByPlaylist(@Path("playlistId") playlistId: Int): Response<List<Song>>

    @DELETE("${Constants.DELETE_PLAYLIST_URL}{playlistId}")
    suspend fun deletePlaylist(@Path("playlistId") playlistId: Int): Response<String>

    @PUT("${Constants.UPDATE_PLAYLIST_URL}{playlistId}")
    suspend fun updatePlaylist(@Path("playlistId") playlistId: Int, @Body playlist: Playlist): Response<Playlist>

    @DELETE("${Constants.DELETE_SONG_BY_PLAYLIST_URL}{playlistId}/{songId}")
    suspend fun deleteSongByPlaylist(@Path("playlistId") playlistId: Int, @Path("songId") songId: Int): Response<String>

    @POST(Constants.INSERT_SONG_BY_PLAYLIST_URL)
    suspend fun addSongByPlaylist(@Body playlistSong: PlaylistSong): Response<PlaylistSong>
}