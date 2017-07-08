package com.haku.controller;

import com.haku.model.AccountService;
import com.haku.model.DataBaseService;
import com.haku.model.Todo;
import com.haku.model.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by QuyPN on 6/22/2017.
 */
@Controller
public class HomeController {

    @Autowired
    TodoService todoService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String home(Model model) throws ClassNotFoundException {
        DataBaseService.getConnection();
        return "loginActivity";
    }

    @RequestMapping(value = "/list-todos", method = RequestMethod.GET)
    public String listTodos(ModelMap model, @RequestParam int idUser) {
        model.addAttribute("todos",
                todoService.getListTodos(idUser));
        model.addAttribute("userid", idUser);

        return "list-todos";
    }

    @RequestMapping(value = "/list-todos", method = RequestMethod.POST)
    public String submitAccoutInfor(HttpServletRequest request, Model model) {
        String viewName = "loginActivity";
        String message = "Incorrect username or password";
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        boolean checkRegister = request.getParameter("register") != null;

        if (!checkRegister) {
            if (AccountService.checkUser(user, pass)) {
                viewName = "/list-todos";
                ArrayList<Todo> listTodos = todoService.getListTodos(AccountService.getIdUser(user, pass));
                for (int i = 0; i < listTodos.size(); i++) {
                    System.out.println(listTodos.get(i).toString());
                }
                model.addAttribute("todos", listTodos);
                model.addAttribute("userid", AccountService.getIdUser(user, pass));
            }
        } else if (checkRegister) {
            if (AccountService.registerAccount(user, pass)) {
                message = "Success";
            }
            else if(!AccountService.registerAccount(user,pass))
            {
                message = "The username already exists";
            }
            viewName = "loginActivity";
        }
        model.addAttribute("message", message);
        return viewName;
    }

    @RequestMapping(value = "/add-todo", method = RequestMethod.GET)
    public String showTodoPage(Model model, @RequestParam int idUser) {

        model.addAttribute("todo", new Todo(idUser, "", null, false));
        return "todo";
    }

    @RequestMapping(value = "/add-todo", method = RequestMethod.POST)
    public String addTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo";
        }
        todo.setDone(false);
        System.out.printf("@Valid: " + todo.toString());
        todoService.addTodo(todo);
        model.clear();
        return "redirect:list-todos?idUser=" + todo.getUserid();
    }

    @RequestMapping(value = "/delete-todo", method = RequestMethod.GET)
    public String deleTodo(ModelMap model, @RequestParam int id, @RequestParam int idUser) {
        todoService.deleteToto(id);
        model.clear();
        return "redirect:list-todos?idUser=" + idUser;
    }

    @RequestMapping(value = "/update-todo", method = RequestMethod.GET)
    public String showTodoUpdate(ModelMap model, @RequestParam int id, @RequestParam int idUser) {
        if (todoService.getItemTodo(id) == null) return "redirect:list-todos?idUser=" + idUser;
        model.addAttribute("todo", todoService.getItemTodo(id));
        return "todoupdate";
    }

    @RequestMapping(value = "/update-todo", method = RequestMethod.POST)
    public String upDatetodo(ModelMap model, @Valid Todo todo, BindingResult result,HttpServletRequest request) {

        if (result.hasErrors()) {
            System.out.printf("error result");
            return "redirect:list-todos?idUser=" + todo.getUserid();
        }
        boolean done = request.getParameter("isdone")!=null;
        todo.setDone(done);
        System.out.println(todo.getJob());
        System.out.printf(todo.getDate().toString());
        todoService.upDateTodo(todo);
        return "redirect:list-todos?idUser=" + todo.getUserid();
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut() {
        return "loginActivity";
    }
}
