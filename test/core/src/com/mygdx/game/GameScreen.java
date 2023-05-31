package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
	Texture backgroundImg;
	Texture homeImg;
	Texture snowImg;
	Music rainMusic;
	Rectangle home;
	Rectangle snow;
	Vector3 touchPos;
	Array<Rectangle> snowdrops;
	long lastDropTime;
	int snowsGatchered;


	public GameScreen (final Drop gam) {
		this.game = gam;
		camera = new OrthographicCamera();
		touchPos = new Vector3();
		camera.setToOrtho(false, 800,480);
		batch = new SpriteBatch();
		snowImg = new Texture("snow.png");
		homeImg = new Texture("home.png");
		backgroundImg = new Texture("background.png");
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("undertreeinsnow.mp3"));
		rainMusic.setLooping(true);
		rainMusic.play();
		home = new Rectangle();
		snow = new Rectangle();
		home.x = 120;
		home.y = 0;
		home.width = 400;
		home.height = 270;

		snowdrops = new Array<Rectangle>();
		spawnRaindrop();
	}
	public  void spawnRaindrop(){
		Rectangle rainDrop = new Rectangle();
		rainDrop.x = MathUtils.random(0, 800 - 64);
		rainDrop.y = 480;
		rainDrop.width = 64;
		rainDrop.height = 64;
		snowdrops.add(rainDrop);

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
			home.x = (int) touchPos.x;
			home.y = (int) touchPos.y;
		}


		if (TimeUtils.nanoTime() - lastDropTime > 1000000000){spawnRaindrop();}
		Iterator<Rectangle> iter = snowdrops.iterator();

		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0) iter.remove();
			if (raindrop.overlaps(home)) {
				snowsGatchered++;
				iter.remove();}
		}

		game.batch.begin();
		game.batch.draw(backgroundImg, 0, 0, 800, 600);

		game.batch.draw(homeImg, 100, 0, 400, 300);

		game.font.setColor(Color.BLACK);
		game.font.draw(game.batch, "SNOW COUNT: " + snowsGatchered, 100, 480);

		for (Rectangle snowdrop: snowdrops){
			game.batch.draw(snowImg, snowdrop.x, snowdrop.y, 20, 20);
		}
		game.batch.end();
	}

	@Override
	public void dispose () {
		snowImg.dispose();
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
