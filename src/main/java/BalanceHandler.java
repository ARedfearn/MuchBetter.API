import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.sql.SQLException;

public class BalanceHandler implements Handler {

    public void handle(Context ctx){
        //The authorization header contains the token
        String token = ctx.getRequest().getHeaders().get("Authorization");
        User user = new User(token);
        try
        {
            //Token is passed and the balance & currency are populated
            user.GetDetails();
            user.Token = null;
        }
        catch (
                SQLException e)
        {
            ErrorHandler error = new ErrorHandler(e.getMessage());
            Response errorResponse = new Response(500, error);
            //Return an error
            ctx.getResponse().status(errorResponse.getStatus()).send(errorResponse.JsonResponse());
        }

        Response userResponse = new Response(200, user);
        //Return the status as OK and the populated members in the User class are returned as JSON
        ctx.getResponse().status(userResponse.getStatus()).send(userResponse.JsonResponse());
    }
}
