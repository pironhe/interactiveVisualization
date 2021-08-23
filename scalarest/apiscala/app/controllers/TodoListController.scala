package controllers

import models.TodoListItem
import scala.collection.mutable
import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

@Singleton
class TodoListController @Inject()(val controllerComponents: ControllerComponents)
extends BaseController {

    implicit val todoListJson = Json.format[TodoListItem]
    private val todoList = new mutable.ListBuffer[TodoListItem]()
    todoList += TodoListItem(1, "test", true)
    todoList += TodoListItem(2, "some other value", false)

    def getAll(): Action[AnyContent] = Action {
        if (todoList.isEmpty) {
            NoContent
        } else {
            Ok(Json.toJson(todoList))
        }
    }
    
}