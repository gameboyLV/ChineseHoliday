import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ChineseHoliday {
    static String specificHolidayUrl = "https://gameboylv.github.io/ChineseHoliday/data/%s.txt";
    static String specificWorkdayUrl = "https://gameboylv.github.io/ChineseHoliday/data/%s_w.txt";
    static LocalTime workStartTime = LocalTime.of(9, 0);
    static LocalTime workEndTime = LocalTime.of(17, 30);

    static LocalDate[] holidayOfYear(int year) {
        var holiday = new ArrayList<LocalDate>();
        var firstDayOfYear = LocalDate.of(year, 1, 1);
        var daysOfYear = firstDayOfYear.plusYears(1).minusDays(1).getDayOfYear();
        for (var i = 1; i <= daysOfYear; i++) {
            var day = firstDayOfYear.withDayOfYear(i);
            if (day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY)
                holiday.add(day);
        }

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest requestHolidays = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(specificHolidayUrl, year)))
                    .build();
            var specificHolidays = client.send(requestHolidays, HttpResponse.BodyHandlers.ofString()).body();
            for (var i : specificHolidays.split("\\n")) {
                if (i.length() != 4) continue;
                var day = LocalDate.of(year, Integer.parseInt(i.substring(0, 2)), Integer.parseInt(i.substring(2, 4)));
                if (!holiday.contains(day)) holiday.add(day);
            }

            HttpRequest requestWorkdays = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(specificWorkdayUrl, year)))
                    .build();
            var specificWorkday = client.send(requestWorkdays, HttpResponse.BodyHandlers.ofString()).body();
            for (var i : specificWorkday.split("\\n")) {
                if (i.length() != 4) continue;
                var day = LocalDate.of(year, Integer.parseInt(i.substring(0, 2)), Integer.parseInt(i.substring(2, 4)));
                if (holiday.contains(day)) holiday.remove(day);
            }

        } catch (Exception ex) {
        }
        Collections.sort(holiday);
        return holiday.toArray(LocalDate[]::new);
    }

    static HashMap<Integer, LocalDate[]> holiday = new HashMap<>();

    public static LocalDate[] getHoliday(int year) {
        if (!holiday.containsKey(year))
            holiday.put(year, holidayOfYear(year));
        return holiday.get(year);
    }

    public static LocalDate[] getHoliday(int year, int month) {
        return Arrays.stream(getHoliday(year))
                .filter(item -> item.getMonth().getValue() == month)
                .toArray(LocalDate[]::new);
    }

    public static Boolean isWorkingDay(LocalDate date) {
        return !Arrays.asList(getHoliday(date.getYear())).contains(date);
    }

    public static Boolean isWorkingTime(LocalDateTime dateTime) {
        var date = dateTime.toLocalDate();
        var time = dateTime.toLocalTime();
        return isWorkingDay(date) && time.isAfter(workStartTime) && time.isBefore(workEndTime);
    }
}
