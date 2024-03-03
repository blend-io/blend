package club.theblend.transforms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTransform implements Transform{
    private static DateTransform instance;
    public static DateTransform getInstance() {
        if(instance == null) {
            instance = new DateTransform();
        }
        return instance;
    }
    @Override
    public Object resolve(Object initialValue, String[] args) {
        String sourceDateTimeString = String.valueOf(initialValue);
        SimpleDateFormat sourceFormat = new SimpleDateFormat(args[0]);
        try {
            Date sourceDate = sourceFormat.parse(sourceDateTimeString);
            SimpleDateFormat transformedFormat = new SimpleDateFormat(args[1]);
            return transformedFormat.format(sourceDate);
        }
        catch(ParseException e) {
            return sourceDateTimeString;
        }
    }
}
