package com.fabAdventure.controllers;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabAdventure.models.Cards;
import com.fabAdventure.models.UserAndCards;
import com.fabAdventure.models.Users;
import com.fabAdventure.models.UsersRequest;
import com.fabAdventure.service.UserService;

@RestController
@CrossOrigin(origins = {"https://fabodyssey-frontend-28fbaa3cb5b9.herokuapp.com",
	"http://localhost:4200", "https://www.fabodyssey.com", "https://fabodyssey.com"})
@RequestMapping("/")

public class UserController {
	@Autowired
	private UserService userService;
    @PostMapping("/user/fetch")
	public UserAndCards fetchAccount(@RequestBody UsersRequest message) {
		try {
			Users user = this.userService.doesUserExist(message.getSlug());
			if (user != null) {
				ArrayList<?> cards = userService.getCards(message.getSlug());
				UserAndCards response = new UserAndCards();
				response.setUser(user);
				response.setCards(cards);
				return response;
			} else {
				return new UserAndCards();
			}
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
			return new UserAndCards();
		}
	}
    	

	@PostMapping("/user/addLevel")
	public void addLevel(@RequestBody UsersRequest message) {
		try {
			this.userService.updateUserLevel(message.getSlug(), message.getUserLevel());
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
    }

	@PostMapping("/user/getCards")
	public void getCards(@RequestBody UsersRequest message) {
		try {
			this.userService.getCards(message.getSlug());
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
    }

	@PostMapping("/admin/resetLevel")
	public void resetLevel(@RequestBody UsersRequest message) {
		try {
			this.userService.resetUserLevel(message.getSlug());
			this.userService.deleteUserCards(message.getSlug());
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
    }

	@PostMapping("/admin/changeUserName")
	public void changeUserName(@RequestBody UsersRequest message) {
		try {
			this.userService.changeUserName(message.getSlug(), message.getUser());
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
    }

	@PostMapping("/admin/changePhone")
	public void changePhone(@RequestBody UsersRequest message) {
		try {
			this.userService.changePhone(message.getSlug(), message.getPhoneNumber());
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
    }

	@PostMapping("/admin/deleteUser")
	public void deleteUser(@RequestBody UsersRequest message) {
		try {
			this.userService.deleteUserCards(message.getSlug());
			this.userService.deleteUser(message.getSlug());
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
    }

	@PostMapping("/user/create")
	public void createAccount(@RequestBody UsersRequest message) {
		try {
			this.userService.creteUser(message.getPhoneNumber(), message.getDeck(), message.getUser(), message.getOriginlocation());
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
    }

	@PostMapping("/user/card")
	public void addCard(@RequestBody UsersRequest message) {
		try{
			this.userService.addCardToUserDeck(message);
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
    }

	@PostMapping("/user/setLocation")
	public void setLocation(@RequestBody UsersRequest message) {
		try{
			this.userService.setLocation(message);
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
	}
	
	@PostMapping("/user/select-cards")
	public void selectCards(@RequestBody UsersRequest message) {
		try{
			this.userService.cardsChosen(message);
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
    }

	@PostMapping("/user/get-select-card")
	public ArrayList<Cards> getSelectCards(@RequestBody UsersRequest message) {
		ArrayList<Cards> cards = new ArrayList<Cards>();
		try{
			cards = this.userService.getCardsChosen(message);
		} catch (Exception e) {
			System.out.println("error e" + e.getMessage().toString());
		}
		return cards;
    }

}
