package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;

public class GameScreen implements Screen {

	final Drop game;
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture dropImg;
	Texture homeImg;

	Music rainMusic;
	Rectangle home;
	Vector3 touchPos;
	Array<Rectangle> raindrops;
	long lastDropTime;
	int dropsGatchered;


	public GameScreen (final Drop gam) {
		this.game = gam;
		camera = new OrthographicCamera();
		touchPos = new Vector3();
		camera.setToOrtho(false, 800,480);
		batch = new SpriteBatch();
		dropImg = new Texture("droplet.png");
		homeImg = new Texture("home.png");
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertreeinrain.mp3"));
		rainMusic.setLooping(true);
		rainMusic.play();
		home = new Rectangle();
		home.x = 0;
		home.y = 0;
		home.width = 64;
		home.height = 64;

		raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}
	public  void spawnRaindrop(){
		Rectangle rainDrop = new Rectangle();
		rainDrop.x = MathUtils.random(0, 800 - 64);
		rainDrop.y = 480;
		rainDrop.width = 64;
		rainDrop.height = 64;
		raindrops.add(rainDrop);

		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(1, 1, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();

		game.batch.setProjectionMatrix(camera.combined);

		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			home.x = (int) touchPos.x - 64 / 2;
			home.y = (int) touchPos.y - 64 / 2;
		}

		// TODO


		if (TimeUtils.nanoTime() - lastDropTime > 1000000000){spawnRaindrop();}
		Iterator<Rectangle> iter = raindrops.iterator();

		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0) iter.remove();
			if (raindrop.overlaps(home)) {
				dropsGatchered++;
				iter.remove();}
		}

		game.batch.begin();
		game.batch.draw(homeImg, 100, 0, 400, 300);
		game.font.draw(game.batch, "SNOW COUNT: " , 0, 480);

		for (Rectangle raindrop: raindrops){
			game.batch.draw(dropImg, raindrop.x, raindrop.y);
		}
		game.batch.end();
	}



	@Override
	public void dispose () {
		dropImg.dispose();
		homeImg.dispose();
		rainMusic.dispose();

	}

	@Override
	public void show() {
		rainMusic.play();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}
}
