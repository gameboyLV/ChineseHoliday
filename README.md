# ChineseHoliday

仅一个类库，无需装包，无需更新，实时在线查询Github最新数据

## C#

#### get holidays by year

ChineseHoliday.GetHoliday(2020)

#### get holidays by month

ChineseHoliday.GetHoliday(2020,1)

#### check whether a date is working day

ChineseHoliday.IsWorkingDay(DateTime.Now.Date)

#### check whether a time is working time

ChineseHoliday.IsWorkingTime(DateTime.Now)


## Java

#### get holidays by year

ChineseHoliday.getHoliday(2020)

#### get holidays by month

ChineseHoliday.getHoliday(2020,1)

#### check whether a date is working day

ChineseHoliday.isWorkingDay(LocalDate.now())

#### check whether a time is working time

ChineseHoliday.isWorkingTime(LocalDateTime.now())
