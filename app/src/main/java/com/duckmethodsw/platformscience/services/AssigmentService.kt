package com.duckmethodsw.platformscience.services

import com.duckmethodsw.platformscience.models.Assigment
import com.duckmethodsw.platformscience.models.AssignmentData

class AssigmentService(assignmentData: AssignmentData) {

    private var assignments: ArrayList<Assigment> = ArrayList()
    private var streetNames: HashMap<String, String> = HashMap()

    init{
        val unassignedDrivers: MutableList<Driver> = assignmentData.drivers.map{ name -> Driver(name)}.toMutableList()
        val unassignedShipments: MutableList<String> = assignmentData.shipments.toMutableList()
        while (unassignedDrivers.size > 0 && unassignedShipments.size > 0) {
            var highestSS = 0f
            var highestDriver : Driver = unassignedDrivers.get(0)
            var highestShipment = ""

            /*Search for the highest SS score across all available drivers and available shipments*/
            unassignedShipments.forEach { shipment ->
                unassignedDrivers.forEach{driver ->
                    if (driver.getSS(shipment) > highestSS){
                        highestSS = driver.getSS(shipment)
                        highestDriver = driver
                        highestShipment = shipment
                    }
                }
            }

            //Assign the driver to the shipment with the highest SS
            assignments.add(Assigment(highestDriver.name, highestShipment))

            //Remove the assigned driver and shipment from the lists
            unassignedDrivers.remove(highestDriver)
            unassignedShipments.remove(highestShipment)
        }

        //If some drivers didn't get assignments (more drivers than shipments), mark them as "No shipment"
        if (unassignedDrivers.isNotEmpty()){
            unassignedDrivers.forEach{driver ->
                assignments.add(Assigment(driver.name, "No shipment"))
            }
        }
    }

    public fun getAssignments() : List<Assigment>{
        return assignments;
    }


    private fun getStreetName(shipment: String): String {
        //First, let's see if we've previously calculated this. If so, return the value
        if (streetNames.contains(shipment)) return streetNames.get(shipment)!!

        /*
        The assumption here is that the shipment's street name does not contain the address
        number or suite/apt number. For example, the street name of
        "63187 Volkman Garden Suite 447" is "Volkman Garden". Given the example data, we can
        split the shipment by spaces:
        1. ignore the first word
        2. ignore "Apt." and "Suite" and any words after that.
        These are not great assumptions, but work with the data that we are given.
         */
        val ignoreWords: List<String> = listOf("Suite", "Apt.")
        val words: List<String> = shipment.split(' ')
        var streetName: String = ""
        for (i in 1..words.lastIndex) {
            if (ignoreWords.contains(words[i])) {
                break;
            }
            streetName += (words[i] + " ")
        }

        //Save off the streetName and remove the extra space
        streetNames.put(shipment, streetName.trim())
        return streetName.trim()
    }

    inner class Driver(val name: String) {
        var vowels: Int = 0
        var consonants: Int = 0
        var spaces: Int = 0
        var length: Int = name.length

        var ssScores: HashMap<String, Float> = HashMap()

        init {
            /*Search the name to find the number of vowels and consonants. We can assume that if
            it's not a vowel or a space that it is a consonant. This also only works with
            the english language
            */
            name.forEach { c ->
                when (c) {
                    'a', 'e', 'i', 'o', 'u' -> vowels++
                    ' ' -> spaces++
                    else -> consonants++
                }
            }
        }

        fun getSS(shipment: String): Float {
            //First, let's see if we've previously calculated this. If so, return the value
            if (ssScores.contains(shipment)) return ssScores.get(shipment)!!

            //If not, let's calculate it
            var ss: Float = 0f;
            //Get the street name
            val streetName = getStreetName(shipment)

            /*Determine if the street name length is even or odd. We're assuming that this includes
            spaces
             */
            if (streetName.length % 2 == 0) { //Even
                /*If the length of the shipment's destination street name is even, the base
                suitability score (SS) is the number of vowels in the driver’s name multiplied
                by 1.5.*/
                ss = vowels * 1.5f
            } else { //Odd
                /*If the length of the shipment's destination street name is odd, the base SS is
                the number of consonants in the driver’s name multiplied by 1. */
                ss = consonants.toFloat()
            }

            /*If the length of the shipment's destination street name shares any common factors
            (besides 1) with the length of the driver’s name, the SS is increased by 50%
            above the base SS.
            Note: This is strangely worded and I'm assuming if the length of the street name
            equals the length of the drivers name, increase the SS by 50%*/
            if (streetName.length == length) ss *= 1.5f

            //Save the ss for future use
            ssScores.put(shipment, ss);
            return ss
        }
    }
}