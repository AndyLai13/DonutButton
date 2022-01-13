package com.andylai.donutbuttons

@Suppress("MemberVisibilityCanBePrivate")
object ColorPicker {

	// ColorTicket is a voucher that users take it to exchange Color they want.
	data class ColorTicket(val colorType: ColorType, val index: Int)

	fun getColorRes(colorTicket: ColorTicket): Int {
		return if (colorTicket.colorType == ColorType.Pen) {
			RES_PEN_COLORS[colorTicket.index]
		} else {
			RES_HIGHLIGHTER_COLORS[colorTicket.index]
		}
	}

	val RES_PEN_COLORS = listOf(
		R.color.Picker1,
		R.color.Picker2,
		R.color.Picker3,
		R.color.Picker4,
		R.color.Picker5,
		R.color.Picker6,
		R.color.Picker7,
		R.color.Picker8,
	)

	val RES_HIGHLIGHTER_COLORS = listOf(
		R.color.Picker9,
		R.color.Picker10,
		R.color.Picker3,
		R.color.Picker4,
		R.color.Picker5,
		R.color.Picker6,
		R.color.Picker7,
		R.color.Picker8,
	)

	val RES_PEN_COLOR_DRAWABLES = listOf(
		R.drawable.ic_donut_pen_picker1,
		R.drawable.ic_donut_pen_picker2,
		R.drawable.ic_donut_pen_picker3,
		R.drawable.ic_donut_pen_picker4,
		R.drawable.ic_donut_pen_picker5,
		R.drawable.ic_donut_pen_picker6,
		R.drawable.ic_donut_pen_picker7,
		R.drawable.ic_donut_pen_picker8,
	)

	val RES_HIGHLIGHTER_COLORS_DRAWABLES = listOf(
		R.drawable.ic_donut_brush_picker9,
		R.drawable.ic_donut_brush_picker10,
		R.drawable.ic_donut_brush_picker3,
		R.drawable.ic_donut_brush_picker4,
		R.drawable.ic_donut_brush_picker5,
		R.drawable.ic_donut_brush_picker6,
		R.drawable.ic_donut_brush_picker7,
		R.drawable.ic_donut_brush_picker8,
	)

	val RES_PEN_COLOR_DRAWABLES_SEL = listOf(
		R.drawable.sel_color_1,
		R.drawable.sel_color_2,
		R.drawable.sel_color_3,
		R.drawable.sel_color_4,
		R.drawable.sel_color_5,
		R.drawable.sel_color_6,
		R.drawable.sel_color_7,
		R.drawable.sel_color_8,
	)

	val RES_HIGHLIGHTER_COLORS_DRAWABLES_SEL = listOf(
		R.drawable.sel_color_9,
		R.drawable.sel_color_10,
		R.drawable.sel_color_3,
		R.drawable.sel_color_4,
		R.drawable.sel_color_5,
		R.drawable.sel_color_6,
		R.drawable.sel_color_7,
		R.drawable.sel_color_8,
	)

	val ColorSize = RES_PEN_COLORS.size

	enum class ColorType {
		Pen,
		HighLighter
	}
}