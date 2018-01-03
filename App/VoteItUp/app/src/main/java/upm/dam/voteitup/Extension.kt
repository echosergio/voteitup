package upm.dam.voteitup

import android.os.Bundle
import java.util.concurrent.ThreadLocalRandom

fun ClosedRange<Double>.random() =
        ThreadLocalRandom.current().nextDouble(endInclusive - start) +  start


