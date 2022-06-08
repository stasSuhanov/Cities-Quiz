package com.mytest.android.citiesapp


import com.mytest.android.citiesapp.utils.RoundPopulation
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RoundPopulationTest {

    @Test
    fun roundPopulationTest() {
        assertThat("no round", RoundPopulation().roundPopulation(-77) == 0)
        assertThat("no round", RoundPopulation().roundPopulation(-9) == 0)
        assertThat("no round", RoundPopulation().roundPopulation(-1) == 0)
        assertThat("no round", RoundPopulation().roundPopulation(0) == 0)
        assertThat("no round", RoundPopulation().roundPopulation(5) == 5)
        assertThat("no round", RoundPopulation().roundPopulation(45) == 45)
        assertThat("no round", RoundPopulation().roundPopulation(95) == 95)
        assertThat("no round", RoundPopulation().roundPopulation(134) == 134)
        assertThat("no round", RoundPopulation().roundPopulation(167) == 167)
        assertThat("no round", RoundPopulation().roundPopulation(688) == 688)
        assertThat("round up", RoundPopulation().roundPopulation(1583) == 1600)
        assertThat("round down", RoundPopulation().roundPopulation(1438) == 1400)
        assertThat("round up", RoundPopulation().roundPopulation(15833) == 16000)
        assertThat("round down", RoundPopulation().roundPopulation(14384) == 14000)
        assertThat("round up", RoundPopulation().roundPopulation(158337) == 160000)
        assertThat("round down", RoundPopulation().roundPopulation(143847) == 140000)
        assertThat("round up", RoundPopulation().roundPopulation(1585337) == 1600000)
        assertThat("round down", RoundPopulation().roundPopulation(1438547) == 1400000)
        assertThat("round up", RoundPopulation().roundPopulation(15833784) == 16000000)
        assertThat("round down", RoundPopulation().roundPopulation(14384764) == 14000000)
        assertThat("round up", RoundPopulation().roundPopulation(158337584) == 160000000)
        assertThat("round down", RoundPopulation().roundPopulation(143847647) == 140000000)
    }
}