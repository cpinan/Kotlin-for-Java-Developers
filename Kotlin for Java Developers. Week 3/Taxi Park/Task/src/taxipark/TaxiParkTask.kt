package taxipark

import kotlin.math.roundToInt

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        allDrivers.filter { driver ->
            trips.count { it.driver.name == driver.name } == 0
        }.toSet()


/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> = allPassengers.filter { passenger ->
    trips.count { trip ->
        trip.passengers.contains(passenger)
    } >= minTrips
}.toSet()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        allPassengers.filter { passenger ->
            trips.filter { trip ->
                trip.passengers.contains(passenger)
            }.count { trip ->
                trip.driver.name == driver.name
            } > 1
        }.toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    return allPassengers.filter { passenger ->
        val tripsFilter = trips.filter { trip -> trip.passengers.contains(passenger) }
        val tripsDiscount = tripsFilter.count { trip -> trip.discount != null && trip.discount > 0 }
        tripsDiscount > tripsFilter.size / 2
    }.toSet()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val max = trips.groupBy { it.duration / 10 }.maxBy { it.value.size } ?: return null
    val value = max.key * 10
    return IntRange(value, value + 9)
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val totalIncome = trips.sumByDouble { it.cost }
    val map = trips.groupBy { trip ->
        trip.driver
    }.map { (_, value) ->
        value.sumByDouble { it.cost }
    }.sortedByDescending {
        it
    }
    val percentage = (totalIncome * 0.8f).roundToInt()
    val minDrivers = (allDrivers.size.toFloat() * 0.2f).toInt()
    val sum = map.filterIndexed { index, _ -> index < minDrivers }.sum().roundToInt()

    return sum > 0 && sum >= percentage

}

fun TaxiPark.findFakeDrivers1(): Set<Driver> =
        allDrivers.filter { driver ->
            trips.none { it.driver == driver }
        }.toSet()

fun TaxiPark.findFakeDrivers2(): Set<Driver> =
        allDrivers - trips.map { it.driver }

fun TaxiPark.findFaithfulPassengers1(minTrips: Int): Set<Passenger> =
        trips
                .flatMap(Trip::passengers)
                .groupBy { it }
                .filter { it.value.size >= minTrips }
                .map { it.key }
                .toSet()

fun TaxiPark.findFaithfulPassengers2(minTrips: Int): Set<Passenger> =
        allPassengers
                .partition { p ->
                    trips.sumBy { t ->
                        if (p in t.passengers) 1 else 0
                    } >= minTrips
                }
                .first
                .toSet()

fun TaxiPark.findSmartPassengers1(): Set<Passenger> {
    val (first, second) = trips.partition { it.discount != null }
    return allPassengers
            .filter { passenger ->
                first.count {
                    it.passengers.contains(passenger)
                } >
                        second.count { it.passengers.contains(passenger) }
            }
            .toSet()

}

fun TaxiPark.findSmartPassengers2(): Set<Passenger> {
    return allPassengers
            .associate { p ->
                p to trips.filter { t -> p in t.passengers }
            }
            .filterValues { group ->
                val (withDiscount, withoutDiscount) = group.partition { it.discount != null }
                withDiscount.size > withoutDiscount.size
            }.keys

}

fun TaxiPark.findTheMostFrequentTripDurationPeriod1(): IntRange? {
    return trips
            .groupBy {
                val start = it.duration / 10 * 10
                val end = start + 9
                start..end
            }
            .toList()
            .maxBy { (_, group) ->
                group.size
            }?.first
}

fun TaxiPark.checkParetoPrinciple2(): Boolean {
    if (trips.isEmpty()) return false

    val totalIncome = trips.sumByDouble(Trip::cost)
    val sortedDriversIncome: List<Double> = trips
            .groupBy(Trip::driver)
            .map { (_, tripsByDriver) -> tripsByDriver.sumByDouble(Trip::cost) }
            .sortedDescending()

    val numberOfTopDrivers = (0.2 * allDrivers.size).toInt()
    val incomeByTopDrivers = sortedDriversIncome
            .take(numberOfTopDrivers)
            .sum()

    return incomeByTopDrivers >= 0.8 * totalIncome
}