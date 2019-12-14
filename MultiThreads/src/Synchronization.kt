import com.sun.istack.internal.Nullable
import java.awt.image.BufferedImage
import java.util.concurrent.CyclicBarrier

class Synchronization(var barrier: CyclicBarrier?, var picture: BufferedImage, var elements: Int) : Thread() {
    var width  = picture.width
    var height  = picture.height
    var pixels = ArrayList<Pixel>()

    fun calculate(threads: Int) {
        barrier = CyclicBarrier(threads + 1)
        val sync = arrayOfNulls<Synchronization>(threads)
        var elements = width * height / threads
        var amount = elements
        for (i in 0 until threads){
            if (i == threads - 1)
                elements += width * height % threads-1

                sync[i] = Synchronization(barrier, picture, elements)

                for (j in (amount * i) .. (amount * (i + 1)) -1 ) {
                        sync[i]?.pixels?.add(Pixel(j/height, j%height))
                }

                sync[i]?.start()
            }

        val startTime = System.nanoTime()
        barrier?.await()
        sync.forEach {
            it?.join()
        }
        val endTime = System.nanoTime()
        val difference = (endTime - startTime) / 1e6
        System.err.println("$threads $difference")

    }

    override fun run() {

        barrier?.await()

        pixels.forEach {
            var rgb = picture.getRGB(it.x, it.y)
            val a = rgb shr 24 and 0xff
            var r = (rgb shr 16 and 0xff)/**0.3).toInt()*/
            var g = (rgb shr 8 and 0xff)/**0.59).toInt()*/
            var b = (rgb and 0xff)/**0.11).toInt()*/
            var avg = (r+g+b)/3
            var newRGB = a shl 24 or (avg shl 16) or (avg shl 8) or avg
            picture.setRGB(it.x,it.y,newRGB)
        }

    }






}