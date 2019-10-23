import ratpack.handling.Context;
import ratpack.handling.Handler;

import static ratpack.jackson.Jackson.fromJson;

public class SpendHandler implements Handler {

    public void handle(Context ctx){
        String token = ctx.getRequest().getHeaders().get("Authorization");
        User user = new User(token);

        //Convert the JSON into a Transaction(Class) and then pass this to the Spend method in User
        ctx.parse(fromJson(Transaction.class)).then(user::Spend);

        //Return the status 201(Created)
        ctx.getResponse().status(201).send("");
    }

}
