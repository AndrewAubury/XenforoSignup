package me.Andrew.BreezeSiteLink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class SiteAPI
{
    private String API_KEY;
    private String API_LINK;

    public SiteAPI()
    {
        this.API_KEY = Main.getInstance().API_KEY;
        this.API_LINK = Main.getInstance().API_LINK;
    }

    public boolean changeUsername(String old, String newName)
    {
        try
        {
            String changeLink = this.API_LINK + "action=editUser&hash=" + this.API_KEY + "&user=" + old + "&username=" + newName;
            JSONObject json = new JSONObject(readUrl(changeLink));
            if ((json.has("Username")) && (json.getString("Username") == newName)) {
                return true;
            }
            return false;
        }
        catch (JSONException e) {}
        return false;
    }

    public boolean userExists(String name)
    {
        try
        {
            String getLink = this.API_LINK + "?action=getUser&hash=" + this.API_KEY + "&value=" + name;
            JSONObject json = new JSONObject(readUrl(getLink));
            if (json.has("user_id")) {
                return true;
            }
            return false;
        }
        catch (JSONException e) {}
        return false;
    }

    public boolean registerUser(Player p, String email, String password)
    {
        try
        {
            String registerLink = this.API_LINK + "?action=register&hash=" + this.API_KEY + "&username=" + p.getName() +
                    "&password=" + password + "&email=" + email +
                    "&user_state=valid&custom_fields=minecraft_uuid=" + p.getUniqueId().toString();
            JSONObject json = new JSONObject(readUrl(registerLink));
            return !json.has("error");
        }
        catch (JSONException e) {}
        return false;
    }

    public boolean canConnect()
    {
        try
        {
            String testLink = this.API_LINK + "?action=getProfilePosts&hash=" + this.API_KEY;
            URL url = new URL(testLink);

            HttpURLConnection request = (HttpURLConnection)url.openConnection();
            request.connect();
            return request.getResponseCode() == 200;
        }
        catch (IOException e) {}
        return false;
    }

    private String readUrl(String urlString)
    {
        BufferedReader reader = null;
        String res = "";
        try
        {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();

            char[] chars = new char['?'];
            int read;
            while ((read = reader.read(chars)) != -1)
            {
                //int read;
                buffer.append(chars, 0, read);
            }
            if (reader != null) {
                reader.close();
            }
            res = buffer.toString();
        }
        catch (IOException localIOException) {}
        return res;
    }
}
