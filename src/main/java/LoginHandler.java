import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.sql.SQLException;

public class LoginHandler implements Handler {

    public void handle(Context ctx){
        User user = new User();
        try
        {
            //As no token is passed a new user is created.
            user.GetDetails();
            user.GetTransaction();
        }
        catch (SQLException e) {
            ErrorHandler error = new ErrorHandler(e.getMessage());
            Response errorResponse = new Response(500, error);
            //Return error
            ctx.getResponse().status(errorResponse.getStatus()).send(errorResponse.JsonResponse());
        }

        Response userResponse = new Response(201, user);
        //Return the status as OK and the populated members in the User class are returned as JSON
        ctx.getResponse().status(userResponse.getStatus()).send(userResponse.JsonResponse());
    }
}
