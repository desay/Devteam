package com.epam.task6.command.impl.handle;

import com.epam.task6.command.Command;
import com.epam.task6.command.CommandException;
import com.epam.task6.dao.DAOException;
import com.epam.task6.dao.impl.ProjectDAOImpl;
import com.epam.task6.dao.impl.UserDAOImpl;
import com.epam.task6.domain.user.User;
import com.epam.task6.resource.ResourceManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by olga on 28.04.15.
 */
public class AssignProject extends Command {

    private static AssignProject instance = new AssignProject();
    /** Initialize activity logger */
    private static Logger logger = Logger.getLogger("activity");

    /** Logger messages */
    private static final String MSG_EXECUTE_ERROR = "logger.error.execute.create.project";

    /** Attributes and parameters */
    private static final String ATTRIBUTE_USER = "user";


    private static final String ATTRIBUTE_PROJECT_NAME = "project_name";
    private static final String ATTRIBUTE_DEVELOPER_NAME = "dev_name";
    private static final String ASSIG_REDERICT_PAGE = "Controller?executionCommand=SHOW_PROJECTS";
    //project_name
    //dev_name


    public static AssignProject getInstance() {
        return instance;
    }

    /**
     *  This method executes the command.
     *
     *  @param request HttpServletRequest object
     *  @param response HttpServletResponse object
     *  @throws CommandException  If command can't be executed.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException{
        User user = (User)request.getSession().getAttribute(ATTRIBUTE_USER);
        String nameProject = request.getParameter(ATTRIBUTE_PROJECT_NAME);
        String devName = request.getParameter(ATTRIBUTE_DEVELOPER_NAME);
        try {
        UserDAOImpl userDAO = UserDAOImpl.getInstance();
        ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();

            int projectId = projectDAO.returnIdByName(nameProject);
            int devId = userDAO.getUserByName(devName);

            projectDAO.updateDevId(projectId, devId);
            projectDAO.updateStatusById(projectId, 1);
        }
        catch (DAOException e){
            throw new CommandException(ResourceManager.getProperty(MSG_EXECUTE_ERROR) + user.getId(), e);
        }
        return(ASSIG_REDERICT_PAGE);
    }
}

