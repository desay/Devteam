package com.epam.task6.command.impl.content;

import com.epam.task6.command.Command;
import com.epam.task6.command.CommandException;
import com.epam.task6.controller.RequestParameterName;
import com.epam.task6.dao.DAOException;
import com.epam.task6.dao.impl.ProjectDAOImpl;
import com.epam.task6.domain.project.Project;
import com.epam.task6.domain.user.User;
import com.epam.task6.resource.ResourceManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * This command get all ready projects with status 2.
 *
 * Created by olga on 29.04.15.
 */
public class GetReadyProject extends Command {
    private static GetReadyProject instance = new GetReadyProject();
    private static final Logger logger = Logger.getLogger(GetReadyProject.class);

    /** Logger messages */
    private static final String MSG_REQUESTED_COMMAND = "logger.activity.employee.show.ready.project";
    private static final String MSG_EXECUTE_ERROR = "logger.error.execute.view.ready.project";

    /** Attributes and parameters */
    private static final String USER_ATTRIBUTE = "user";

    /** Forward page */
    private static final String NEW_PROJECT_PAGE = "jsp/developer/newProjects.jsp";

    public static GetReadyProject getInstance() {
        return instance;
    }

    /**
     *  mplementation of command that get all ready projects.
     *
     *  @param request HttpServletRequest object
     *  @param response HttpServletResponse object
     *  @return rederict page or command
     *  @throws CommandException  If command can't be executed.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        User user = (User)request.getSession().getAttribute(USER_ATTRIBUTE);
        ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
        try {
            List<Project> projectList = projectDAO.getProjectsByStatusAndDivId(2 ,user.getId());
            if (null != projectList) {
                request.setAttribute(RequestParameterName.SIMPLE_INFO, projectList);
            }
        }
        catch (DAOException e){
            throw new CommandException(ResourceManager.getProperty(MSG_EXECUTE_ERROR) + user.getId(), e);
        }
        logger.info(ResourceManager.getProperty(MSG_REQUESTED_COMMAND)+user.getId());
        return NEW_PROJECT_PAGE;
    }
}