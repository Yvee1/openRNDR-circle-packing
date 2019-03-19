import org.openrndr.Fullscreen
import org.openrndr.application
import org.openrndr.color.ColorRGBa

import org.openrndr.draw.FontImageMap
import org.openrndr.draw.loadImage
import org.openrndr.draw.tint
import org.openrndr.extensions.Screenshots
import org.openrndr.ffmpeg.ScreenRecorder
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

fun main() = application {
    configure {
        width = 1920
        height = 1080
        fullscreen = Fullscreen.SET_DISPLAY_MODE
    }

    program {

        var circles = mutableListOf<Circle>()

        var numFrames = 0
        extend(Screenshots())
        extend {


            for (i in 1..100) {
                var circle = Circle(width.toDouble(), height.toDouble())



                while (true) {
                    var found = true
                    for (c in circles) {
                        if (circle.inside(c)) {
                            circle = Circle(width.toDouble(), height.toDouble())
                            found = false
                            break
                        }
                    }
                    if (found) {
                        break
                    }
                }
                while (true) {
                    var found = true
                    for (c in circles) {
                        if (c.collides(circle) || circle.wall(width.toDouble(), height.toDouble())) {
                            circle.r = circle.r - 1
                            found = false
                            break
                        }
                    }
                    if (found) {
                        break
                    }
                }
                if(circle.r >= 1)
                circles.add(circle)

            }

            for (c in circles){
                drawer.fill = ColorRGBa.fromHex(c.color)
                drawer.stroke = ColorRGBa.fromHex(c.color)
                drawer.circle(c.x, c.y, c.r)

            }

            // Same color circles
//            var circleShapes = mutableListOf<org.openrndr.shape.Circle>()
//
//            for (c in circles){
//                circleShapes.add(org.openrndr.shape.Circle(c.x, c.y, c.r))
//            }
//
//            drawer.stroke = ColorRGBa.WHITE
//            drawer.circles(circleShapes)
            //drawer.circle(circle.x.toDouble(), circle.y.toDouble(), circle.r.toDouble())
            if (numFrames%60==0) {
                println(numFrames / seconds)
            }
            numFrames++

        }
    }
}

class Circle(w : Double, h : Double) {
    val x = Random.nextDouble(w)
    val y = Random.nextDouble(h)
    var r = Random.nextDouble(20.0, h/5.0)

    val color = listOf(0xDD6649, 0x7EA172, 0xC7CB85, 0xE7A977, 0xEBBE9B)[Random.nextInt(5)]
    //val color = listOf(0x502419, 0x7EA172, 0xC7CB85, 0xE7A977, 0xEBBE9B)[Random.nextInt(5)]

    fun inside(other: Circle): Boolean {
        return this.dist(other) <= other.r
    }

    fun collides(other: Circle): Boolean {
        return this.dist(other) <= this.r + other.r
    }

    fun dist(other: Circle): Double {
        return sqrt((x - other.x).pow(2)+ (y - other.y).pow(2))
    }

//    fun dist(x_: Double, y_: Double): Double{
//        return sqrt((x - x_).pow(2)+ (y - y_).pow(2))
//    }

    fun wall(w: Double, h: Double): Boolean{
        return x + r > w || x - r < 0 || y + r > h || y - r < 0
    }
}