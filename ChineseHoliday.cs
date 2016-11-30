using System;
using System.Collections.Generic;
using System.Net;

public class ChineseHoliday
{
    static string specificHolidayUrl = "http://gameboylv.github.io/chineseholiday/data/{0}.txt";
    static string specificWorkdayUrl = "http://gameboylv.github.io/chineseholiday/data/{0}_w.txt";
    static TimeSpan workStartTime = new TimeSpan(9, 0, 0);
    static TimeSpan workEndTime = new TimeSpan(17, 30, 0);

    static List<DateTime> holidayOfYear(int year)
    {
        var holiday = new List<DateTime>();
        for (var i = new DateTime(year, 1, 1); i < new DateTime(year + 1, 1, 1); i = i.AddDays(1))
        {
            if (i.DayOfWeek == DayOfWeek.Saturday || i.DayOfWeek == DayOfWeek.Sunday)
                holiday.Add(i);
        }

        try
        {
            var specificHolidays = new WebClient().DownloadString(string.Format(specificHolidayUrl, year));
            foreach (var i in specificHolidays.Split(new[] { '\r', '\n' }, StringSplitOptions.RemoveEmptyEntries))
            {
                var day = DateTime.Parse(year + "-" + i.Substring(0, 2) + "-" + i.Substring(2, 2));
                if (!holiday.Contains(day))
                    holiday.Add(day);
            }

            var specificWorkday = new WebClient().DownloadString(string.Format(specificWorkdayUrl, year));
            foreach (var i in specificWorkday.Split(new[] { '\r', '\n' }, StringSplitOptions.RemoveEmptyEntries))
            {
                var day = DateTime.Parse(year + "-" + i.Substring(0, 2) + "-" + i.Substring(2, 2));
                if (holiday.Contains(day))
                    holiday.Remove(day);
            }
        }
        catch
        {

        }
        holiday.Sort();
        return holiday;
    }

    static Dictionary<int, List<DateTime>> holiday = new Dictionary<int, List<DateTime>>();

    public static List<DateTime> GetHoliday(int year)
    {
        if (!holiday.ContainsKey(year))
            holiday[year] = holidayOfYear(year);
        return holiday[year];
    }

    public static bool IsWorkingTime(DateTime time)
    {
        return !GetHoliday(time.Year).Contains(time.Date) && time.TimeOfDay >= workStartTime && time.TimeOfDay < workEndTime;
    }

}