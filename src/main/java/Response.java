import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Response{
    private int Status;

    public int getStatus() {
        return this.Status;
    }

    private User User;
    private ErrorHandler Error;

    public Response(int status, User response){
        Status = status;
        User = response;
    };

    public Response(int status, ErrorHandler error){
        Status = status;
        Error = error;
    };

    public String JsonResponse() {
         return ConvertToJSON();
    }

    private String ConvertToJSON(){
        //Here the class is converted to a JSON format. Each populated property is returned.
        final byte[] data;
        try
        {
            String output;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            //Formatting the transaction date returned within the JSON.
            DateFormat df = new SimpleDateFormat("dd-MM-yy");
            mapper.setDateFormat(df);

            //Pretty printer will properly indent the JSON.
            mapper.writerWithDefaultPrettyPrinter().writeValue(out, (User == null) ? Error : User);
            data = out.toByteArray();
        }
        catch (IOException e)
        {
            Status = 500;
            return "[ {Error : " + e.getMessage() + "}]";
        }

        return (new String(data));
    }
}
