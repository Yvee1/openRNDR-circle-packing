import org.openrndr.Fullscreen
import org.openrndr.application
import org.openrndr.color.ColorRGBa

import org.openrndr.draw.FontImageMap
import org.openrndr.draw.loadImage
import org.openrndr.draw.shadeStyle
import org.openrndr.draw.tint
import org.openrndr.extensions.Screenshots
import org.openrndr.ffmpeg.ScreenRecorder
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

fun main() = application {
    configure {
        width = 500
        height = 500
        //fullscreen = Fullscreen.SET_DISPLAY_MODE
    }

    program {

        var circles = mutableListOf<Circle>()
        val bigRadius : Double = (height.toDouble() - 20.0)/2  // radius of big circle
        var numFrames = 0
        extend(Screenshots())
        extend {

            drawer.shadeStyle = shadeStyle {
                //fragmentTransform = """
 //float c = smoothstep(-0.01, 0.01, cos(c_element + p_time + c_boundsPosition.y )) * 0.5 + 0.5;
 //x_fill.a *= c;
//                    """.trimMargin()
//                parameter("time", seconds)
//            }
                fragmentTransform = """
                x_fill.rgb *= 1.0-(length(c_boundsPosition.xy - p_position)*0.25);
            """
                parameter("position", mouse.position / window.size)
            }


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
                    if (circle.wall(width.toDouble()/2, height.toDouble()/2, bigRadius)) {
                        circle.r = circle.r - 1
                        found = false

                    }
                    for (c in circles) {
                        if (c.collides(circle)) {
                            circle.r = circle.r - 1
                            found = false
                            break
                        }
                    }
                    if (found) {
                        break
                    }
                }
                if(circle.r >= 1){
                circles.add(circle)
                }

            }

            drawer.background(50.0/255.0, 50.0/255.0, 50.0/255.0, 1.0)
            //drawer.strokeWeight = 5.0
            //drawer.stroke = ColorRGBa.WHITE
            //drawer.fill = null
            //drawer.circle(width.toDouble()/2.0, height.toDouble()/2, bigRadius)
            for (c in circles){
                drawer.fill = ColorRGBa.fromHex(c.color)
                drawer.stroke = null
                drawer.strokeWeight = 0.0
                //drawer.stroke = ColorRGBa.fromHex(c.color)
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

    val color = listOf(0xDD6649, 0x3F577F, 0x93B7BE, 0xBFDBF7, 0x68B684, 0xffffff)[Random.nextInt(6)]
    //val color = listOf(0x502419, 0x7EA172, 0xC7CB85, 0xE7A977, 0xEBBE9B)[Random.nextInt(5)]

    fun inside(other: Circle): Boolean {
        return this.dist(other) <= other.r
    }

    fun collides(other: Circle): Boolean {
        return this.dist(other) < this.r + other.r-2
    }

    fun dist(other: Circle): Double {
        return sqrt((x - other.x).pow(2)+ (y - other.y).pow(2))
    }

    fun dist(ox : Double, oy : Double): Double {
        return sqrt((x - ox).pow(2)+ (y - oy).pow(2))
    }

//    fun dist(x_: Double, y_: Double): Double{
//        return sqrt((x - x_).pow(2)+ (y - y_).pow(2))
//    }

    fun wall(cx: Double, cy: Double, cr : Double): Boolean{
        return cr - this.dist(cx, cy) < this.r
    }
}