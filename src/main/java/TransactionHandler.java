import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.sql.SQLException;

public class TransactionHandler implements Handler {

    public void handle(Context ctx){
        String token = ctx.getRequest().getHeaders().get("Authorization");
        User user = new User(token);
        try
        {
            //Populate the Transaction member
            user.GetTransaction();
            user.Token = null;
            user.Balance = null;
        }
        catch (SQLException e)
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
