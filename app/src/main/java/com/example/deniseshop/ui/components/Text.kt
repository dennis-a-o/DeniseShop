package com.example.deniseshop.ui.components

import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.core.view.setPadding
import coil.Coil
import coil.ImageLoader
import coil.request.ImageRequest

@Composable
fun HtmlText(
	html: String,
	modifier: Modifier = Modifier,
){
	AndroidView(
		modifier = modifier,
		factory = { context -> TextView(context) },
		update = {
			it.text = HtmlCompat.fromHtml(
				html,
				HtmlCompat.FROM_HTML_MODE_COMPACT,
				CoilImageGetter(it),
				null
			)
		}
	)
}

open class CoilImageGetter(
	private val textView: TextView,
	private val imageLoader: ImageLoader = Coil.imageLoader(textView.context),
	private val sourceModifier: ((source: String) -> String)? = null
) : Html.ImageGetter {

	override fun getDrawable(source: String): Drawable {
		val finalSource = sourceModifier?.invoke(source) ?: source

		val drawablePlaceholder = DrawablePlaceHolder()
		imageLoader.enqueue(ImageRequest.Builder(textView.context).data(finalSource).apply {
			target { drawable ->
				drawablePlaceholder.updateDrawable(drawable)
				// invalidating the drawable doesn't seem to be enough...
				textView.text = textView.text
			}
		}.build())
		// Since this loads async, we return a "blank" drawable, which we update
		// later
		return drawablePlaceholder
	}

	@Suppress("DEPRECATION")
	private class DrawablePlaceHolder : BitmapDrawable() {

		private var drawable: Drawable? = null

		override fun draw(canvas: Canvas) {
			drawable?.draw(canvas)
		}

		fun updateDrawable(drawable: Drawable) {
			this.drawable = drawable
			val width = drawable.intrinsicWidth
			val height = drawable.intrinsicHeight
			drawable.setBounds(0, 0, width, height)
			setBounds(0, 0, width, height)
		}
	}
}