package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {
	UserStorage storage = new InMemoryUserStorage();
	FilmStorage filmStorage = new InMemoryFilmStorage();
	UserService serviceUser = new UserService();
	UserController userController = new UserController(serviceUser);
	LocalDate dateOriginal = LocalDate.of(2014, 9, 25);
	LocalDate dateRos = LocalDate.of(1893, 6, 12);
	Film filmNotName = new Film(" ",
			"Ученый пытается опровергнуть Бога.",
			dateOriginal,108);
	Film filmDescription200 = new Film("Я - начало", "В начале, когда на экране появляется" +
			" название фильма, некоторое время видны только буквы I и O," +
			" что составляет «IOII». В двоичной системе счисления 1011 равняется " +
			"числу 11. Это число появляется несколько раз на протяжении всего фильма и" +
			" играет немаловажную роль в развитии сюжета. " +
			"Главный герой Ян разделяет имя с профессором Яном Стивенсоном " +
			"(1918—2007), наиболее известным своими научными " +
			"исследованиями в области реинкарнации.", dateOriginal, 108);
	Film film1893 = new Film("Вокруг кабинки", "мультфильм, короткометражка",
			dateRos, 2);
	Film filmDurationNegative = new Film("Я - начало",
			"Ученый пытается опровергнуть Бога.",
			dateOriginal,-108);
	FilmService service = new FilmService(filmStorage,serviceUser);
	FilmController filmController = new FilmController(service);

	LocalDate birthday = LocalDate.of(1990, 12,1);
	LocalDate birthdayFuture = LocalDate.of(3091, 12,1);
	User userEmailNull = new User(" ", "Kis", "Kis", birthday);
	User userLoginNull = new User("kis@mail.ru", " ", "Kis", birthday);
	User userBirthdayInTheFuture = new User("kis@mail.ru",
			"Kis", "Kis", birthdayFuture);
	User userNameNull = new User("kis@mail.ru", "Kis", "", birthday);


	@Test
	void contextLoads() {
	}

	//Тесты валидации полей для создания фильма
	@Test
	public void shouldThrowExceptionWhenFilmNameNull() {
		ValidationException ex = Assertions.assertThrows(
				ValidationException.class,
				() -> {
					filmController.create(filmNotName);
				}
		);
		assertEquals("Название фильма не может быть пустым.", ex.getMessage());
	}

	@Test
	public void shouldThrowExceptionWhenFilmDescriptionMoreThan200Characters() {
		ValidationException ex = Assertions.assertThrows(
				ValidationException.class,
				() -> {
					filmController.create(filmDescription200);
				}
		);
		assertEquals("Максимальная длина описания - 200 символов.", ex.getMessage());
	}

	@Test
	public void shouldThrowExceptionWhenFilmReleaseDateIsBefore1895year12month28day() {
		ValidationException ex = Assertions.assertThrows(
				ValidationException.class,
				() -> {
					filmController.create(film1893);
				}
		);
		assertEquals("Дата релиза - не раньше 28 декабря 1895.", ex.getMessage());
	}

	@Test
	public void shouldThrowExceptionWhenFilmDurationNegative() {
		ValidationException ex = Assertions.assertThrows(
				ValidationException.class,
				() -> {
					filmController.create(filmDurationNegative);
				}
		);
		assertEquals("Продолжительность фильма должна быть положительной.", ex.getMessage());
	}


	//Тесты валидации полей при создании пользователя
	@Test
	public void shouldThrowExceptionWhenUserEmailNull() {
		ValidationException ex = Assertions.assertThrows(
				ValidationException.class,
				() -> {
					userController.create(userEmailNull);
				}
		);
		assertEquals("Адрес электронной почты не может быть " +
				"пустым и должен содержать символ @.", ex.getMessage());
	}

	@Test
	public void shouldSetNameUserLogin() {
		userController.create(userNameNull);
		assertEquals(userNameNull.getLogin(), userNameNull.getName());
	}

	@Test
	public void shouldThrowExceptionWhenUserLoginNull() {
		ValidationException ex = Assertions.assertThrows(
				ValidationException.class,
				() -> {
					userController.create(userLoginNull);
				}
		);
		assertEquals("Логин не может быть пустым и содержать пробелы.", ex.getMessage());
	}

	@Test
	public void shouldThrowExceptionWhenUserBirthdayIsAfterNow() {
		ValidationException ex = Assertions.assertThrows(
				ValidationException.class,
				() -> {
					userController.create(userBirthdayInTheFuture);
				}
		);
		assertEquals("Дата рождения не может быть в будущем.", ex.getMessage());
	}

}
