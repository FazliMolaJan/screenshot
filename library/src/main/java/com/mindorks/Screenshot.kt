package com.mindorks

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.view.View
import java.io.ByteArrayOutputStream

/*
 * @author Himanshu-Singh
 * Initialise the class by passing the reference of activity
 */
class Screenshot(private val activity: Activity) {

    /*
    * Take Screenshot of the complete screen
    * @return bitmap
    */

    fun getScreenshot(): Bitmap {
        val fullView = activity.window.decorView.rootView
        return getScreenshot(fullView)
    }

    /*
    * Take Screenshot of a specific view
    * Here you need to pass the view you need to get the screenshot
    * @param view
    * @return bitmap
    */

    fun getScreenshot(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        view.background.also {
            it.draw(canvas)
        }
        view.run {
            draw(canvas)
        }
        return returnedBitmap
    }

    /*
    * Take Screenshot of a specific view with specific quality
    * @param view, quality
    * @return bitmap
    */

    fun getScreenshot(view: View, quality: Quality): Bitmap {
        val stream = ByteArrayOutputStream()
        val qualityOutput = when (quality) {
            Quality.HIGH -> 100
            Quality.MEDIUM -> 75
            Quality.AVERAGE -> 50
            Quality.LOW -> 25
        }
        getScreenshot(view).run {
            compress(Bitmap.CompressFormat.JPEG, qualityOutput, stream)
        }
        val byteArray = stream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
    /*
     * Take Screenshot of the bitmap flipping it horizontally or vertically
     * @ param view
     * @ return bitmap
     */

    fun getScreenshot(view: View, flip: Flip): Bitmap {
        val cx = getScreenshot(view).width / 2f
        val cy = getScreenshot(view).height / 2f
        return when (flip) {
            Flip.HORIZONTALLY -> getScreenshot(view).flip(-1f, 1f, cx, cy)
            Flip.VERTICALLY -> getScreenshot(view).flip(1f, -1f, cx, cy)
        }

    }

    fun getScreenshot(flip: Flip): Bitmap {
        val fullView = activity.window.decorView.rootView
        val cx = getScreenshot(fullView).width / 2f
        val cy = getScreenshot(fullView).height / 2f
        return when (flip) {
            Flip.HORIZONTALLY -> getScreenshot(fullView).flip(-1f, 1f, cx, cy)
            Flip.VERTICALLY -> getScreenshot(fullView).flip(1f, -1f, cx, cy)
        }
    }


    private fun Bitmap.flip(x: Float, y: Float, cx: Float, cy: Float): Bitmap {
        val matrix = Matrix().apply { postScale(x, y, cx, cy) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

}