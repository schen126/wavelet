import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    int num = 0;
    ArrayList<String> searches = new ArrayList<String>();

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            return String.format(
                "You have %d searches so far: %s", 
                searches.size(),
                searches.toString()
            );
        } else {
            System.out.println("Path: " + url.getPath());
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    searches.add(parameters[1]);
                    if(searches.size()==1) {
                        return String.format(
                        "You have added %s to the list! You have searched %d item.",
                        parameters[1], 
                        searches.size()
                    );
                    }
                    return String.format(
                        "You have added %s to the list! You have searched %d items.",
                        parameters[1], 
                        searches.size()
                    );
                }
            }
            if (url.getPath().contains("/search")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    String toFind = parameters[1];
                    ArrayList<String> found = new ArrayList<String>();
                    if(searches.size()==0) {
                        return "You haven't searched anything yet.";
                    }
                    for(int i = 0; i < searches.size(); i++) {
                        if(searches.get(i).contains(toFind)) {
                            found.add(searches.get(i));
                        }
                    }
                    if(found.size()==0) {
                        return "Sorry, we couldn't find this search.";
                    }
                    return String.format(
                        "Here are your searches containing \"%s\": %s",
                        toFind, 
                        found.toString()
                    );
                }
            }

            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
