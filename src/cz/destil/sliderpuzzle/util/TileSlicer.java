package cz.destil.sliderpuzzle.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import cz.destil.sliderpuzzle.data.Coordinate;
import cz.destil.sliderpuzzle.ui.TileView;

/**
 * 
 * Slices original bitmap into tiles and adds border. Provides randomized or
 * ordered access to tiles.
 * 
 * Based on
 * https://github.com/thillerson/Android-Slider-Puzzle/blob/master/src/com
 * /tackmobile/TileServer.java
 * 
 * @author David Vavra
 */
public class TileSlicer {

	private Bitmap original;
	private int xtileSize, gridSize, ytileSize;
	private List<Bitmap> slices;
	private int lastSliceServed;
	private List<Integer> sliceOrder;
	private Context context;
	private List<Integer> masterTileOrder;

	/**
	 * Initializes TileSlicer.
	 * 
	 * @param original
	 *            Bitmap which should be sliced
	 * @param gridSize
	 *            Grid size, for example 4 for 4x4 grid
	 */
	public TileSlicer(Bitmap original, int gridSize, Context context) {
		super();
		this.original = original;
		this.gridSize = gridSize;
		this.xtileSize = original.getWidth() / gridSize;
		this.ytileSize = original.getHeight() / gridSize;
		this.context = context;
		slices = new LinkedList<Bitmap>();
		sliceOriginal();
	}

	/**
	 * Slices original bitmap and adds border to slices.
	 */
	private void sliceOriginal() {
		int x, y;
		//Bitmap newOriginal = Bitmap.createScaledBitmap(original, original.getHeight(), original.getHeight(), true);
		Bitmap bitmap;
		lastSliceServed = 0;
		for (int rowI = 0; rowI < gridSize; rowI++) {
			for (int colI = 0; colI < gridSize; colI++) {
				// don't slice last part - empty slice
				if (rowI == gridSize - 1 && colI == gridSize - 1) {
					continue;
				} else {
					x = rowI * xtileSize;
					y = colI * ytileSize;
					// slice
					bitmap = Bitmap.createBitmap(original, x, y, xtileSize, ytileSize);
					// draw border lines
					Canvas canvas = new Canvas(bitmap);
					Paint paint = new Paint();
					paint.setColor(Color.parseColor("#fbfdff"));
					int endx = xtileSize - 1;
					int endy = ytileSize - 1;
					canvas.drawLine(0, 0, 0, endy, paint);
					canvas.drawLine(0, endy, endx, endy, paint);
					canvas.drawLine(endx, endy, endx, 0, paint);
					canvas.drawLine(endx, 0, 0, 0, paint);
					slices.add(bitmap);
				}
			}
		}
		// remove reference to original bitmap
		original = null;
		//masterSliceOrder = getMasterSliceOrder(order)
	}

	/**
	 * Randomizes slices in case no previous state is available.
	 */
	public void randomizeSlices() {
		// randomize first slices
		Collections.shuffle(slices);
		// last one is empty slice
		slices.add(null);
		sliceOrder = null;
	}

	/**
	 * Sets slice order in case of previous instance is available, eg. from
	 * screen rotation.
	 * 
	 * @param order
	 *            list of integers marking order of slices
	 */
	public void setSliceOrder(List<Integer> order) {
		List<Bitmap> newSlices = new LinkedList<Bitmap>();
		for (int o : order) {
			if (o < slices.size()) {
				newSlices.add(slices.get(o));
			} else {
				// empty slice
				newSlices.add(null);
			}
		}
		sliceOrder = order;
		slices = newSlices;
	}

	/**
	 * Serves slice and creates a tile for gameboard.
	 * 
	 * @return TileView with the image or null if there are no more slices
	 */
	public TileView getTile() {
		TileView tile = null;
		if (slices.size() > 0) {
			int originalIndex;
			if (sliceOrder == null) {
				originalIndex = lastSliceServed++;
			} else {
				originalIndex = sliceOrder.get(lastSliceServed++);
			}
			tile = new TileView(context, originalIndex);
			if (slices.get(0) == null) {
				// empty slice
				tile.setEmpty(true);
			}
			tile.setImageBitmap(slices.remove(0));
		}
		return tile;
	}
/*	public void getMasterSliceOrder() {
		List<Bitmap> newSlices = new LinkedList<Bitmap>();
		for (int o : order) {
			if (o < slices.size()) {
				newSlices.add(slices.get(o));
			} else {
				// empty slice
				newSlices.add(null);
			}
		}
		return masterSliceOrder = order;
		//slices = newSlices;
		
		for (int rowI = 0; rowI < 3; rowI++) {
			for (int colI = 0; colI < 3; colI++) {
				TileView tile;
				if (masterTileOrder == null) {
					tile = getTile();
				} else {
					tile = getTile();
				}
				tile.coordinate = new Coordinate(rowI, colI);
				if (tile.isEmpty()) {
					TileView emptyTile = tile;
				}
				//placeTile(tile);
				//tiles.add(tile);
			}
		}
	}*/

}
