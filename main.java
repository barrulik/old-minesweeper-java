import java.util.*;


public class Main {
	static Mine[] mines;

	public static void exposeAllMine(String[][] map, Mine[] mines) {
		for (int i = 0; i < mines.length; i++) {
			Mine mine = mines[i];
			map[mine.x][mine.y] = "💣";
		}
	}

	public static void exposeNear(int x, int y, String[][] playerVision, String[][] map) {
		expose(x, y, playerVision, map);
		for (int r = x - 1; r <= x + 1; r++) {
			for (int c = y - 1; c <= y + 1; c++) {
				if (r >= 0 && c >= 0 && r < map.length && c < map.length) {
					if (playerVision[r][c] == "⬛" && map[x][y] == "0") {
						if (r != x || c != y)
							exposeNear(r, c, playerVision, map);
					}
				}
			}
		}
	}

	public static void expose(int x, int y, String[][] vision, String[][] map) {
		vision[x][y] = map[x][y];
	}

	public static void blank(String[][] a) {
		for (int r = 0; r < a.length; r++) {
			for (int c = 0; c < a[r].length; c++) {
				a[r][c] = "⬛";
			}
		}
	}

	public static boolean mineExistAt(int x, int y, Mine[] mines, int i) {
		for (int j = 0; j < i; j++) {
			if (mines[j].getX() == x && mines[j].getY() == y) {
				return true;
			}
		}
		return false;
	}

	public static String[][] generateMap(int  width, int height) {
		String[][] map = new String[height][width];
		init(map);
		mines = new Mine[(int)(width * height * .12)];
		for (int i = 0; i < mines.length; i++) {
			int x = (int)(Math.random() * height);
			int y = (int)(Math.random() * width);

			while (mineExistAt(y, x,  mines, i)) {
				x = (int)(Math.random() * height);
				y = (int)(Math.random() * width);
			}
			mines[i] = new Mine(x, y);
			map[x][y] = "💣";
			for (int r = x - 1; r <= x + 1; r++) {
				for (int c = y - 1; c <= y + 1; c++) {
					if (r >= 0 && c >= 0 && r < height && c < width)
						if (map[r][c] != "💣")
							map[r][c] = "" + (1 + Integer.parseInt(map[r][c]));
				}
			}
		}
		return map;
	}

	public static void init(String[][] map) {
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				map[r][c] = "0";
			}
		}
	}
	public static void printMap(String[][] map) {
		String seperator = "";
		for (int r = 0; r < map.length; r++) seperator += "--";
		System.out.println(seperator);
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				System.out.print(map[r][c]);
				if (map[r][c] != "💣" && map[r][c] != "⬛" || map[r][c] == "💣")
					System.out.print("‎ ‎");
			}
			System.out.println();
		}
		System.out.println(seperator);
	}

	public static class Mine {
		private int x = 0;
		private int y = 0;
		public Mine(int x, int y) {
			this.x = x;
			this.y = y;
		}

		int getX() {
			return this.x;
		}
		int getY() {
			return this.y;
		}


		String toString_() {
			return x + ", " + y;
		}
	}

	public static void main(String[] args) {
		int height = 10;
		int width = 10;
		boolean lost = false;
		Scanner in = new Scanner(System.in);


		String[][] map = generateMap(width, height);
		String [][] playerVision = new String[height][width];
		blank(playerVision);

		printMap(playerVision);

		while (!lost) {
			String[] arg = in.nextLine().split(" ");
			if (!arg[0].equals("flag") && !arg[0].equals("unflag")) {
				int x = Integer.parseInt(arg[0]);
				int y = Integer.parseInt(arg[1]);

				if (map[x][y] == "0") {
					exposeNear(x, y, playerVision, map);
				}
				expose(x, y, playerVision, map);
				if (map[x][y] == "💣") {
					System.out.println("you lost");
					exposeAllMine(playerVision, mines);
					lost = true;
				}
				printMap(playerVision);
			} else {
				int x = Integer.parseInt(arg[1]);
				int y = Integer.parseInt(arg[2]);
				if (playerVision[x][y] != "⬛" && playerVision[x][y] != "🚩")
					System.out.println("cant put flag on a number");
				else {
					if (arg[0].equals("flag"))
						playerVision[x][y] = "🚩";
					else 
						playerVision[x][y] = "⬛";
				}
				printMap(playerVision);
			}
		}


	}
}
