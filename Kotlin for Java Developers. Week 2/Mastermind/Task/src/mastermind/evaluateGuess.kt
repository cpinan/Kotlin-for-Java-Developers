package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    if (secret == guess) {
        return Evaluation(secret.length, 0)
    }
    var rightPosition = 0
    var wrongPosition = 0

    val secretArray = secret.toCharArray()
    val guessArray = guess.toCharArray()

    for(i in 0 until secret.length) {
        if(secretArray[i] == guess[i]) {
            rightPosition++
            secretArray[i] = '0'
            guessArray[i] = '0'
        }
    }

    for(i in 0 until secret.length) {
        if(secretArray[i] != '0') {
            for(j in 0 until secret.length) {
                if(guessArray[j] != '0' && secretArray[i] == guessArray[j]) {
                    secretArray[i] = '0'
                    guessArray[j] = '0'
                    wrongPosition++
                }
            }
        }
    }


    return Evaluation(rightPosition, wrongPosition)
}
