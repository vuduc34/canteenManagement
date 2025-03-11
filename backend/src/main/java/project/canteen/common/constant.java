package project.canteen.common;

public class constant {
    public static final class TYPE {
        public static final String IN = "in";
        public static final String OUT = "out";
    }
    public static final class STATUS {
        public static final Long ACTIVE = 1L;
        public static final Long DE_ACTIVE = 0L;
        public static  final String AVAILABLE = "available";
        public static  final String UN_AVAILABLE = "unavailable";
        public static  final String UN_CONFIRMED = "unconfirmed";
        public static  final String PREPARING = "preparing";
        public static  final String DONE = "done";
        public static  final String CANCEL = "cancel";
        public static final String REJECTED = "rejected";
        public static final String DELETEED = "deleted";
    }

    public static final class RESULT_CODE {
        public static final Long SUCCESS = 0L;
        public static final Long ERROR = -1L;
        public static final Long NOT_FOUND = 404L;
        public static final Long NOT_ALLOWED = 405L;
        public static final Long UNAUTHORIZED = 403L;


    }

    public static final class API {

        public static final String PREFIX = "/api/v1/project";
        public static final String PREFIX_AUTH = "/api/v1/project/auth";
    }

    public static final class ACTION {

        public static final String CANCEL = "cancel";
        public static final String CONFIRM = "confirm";
        public static final String DONE = "done";
        public static final String REJECTED = "rejected";

    }

    public static final class MESSAGE {
        public static final String SUCCESS = "Successfully";
        public static final String ERROR = "Error";
        public static final String NOT_FOUND_USER = "Not found user in system";
        public static final String PASSWORD_INCORRECT = "Password incorrect";
        public static final String USERNAME_EXIST = "Username already exist";
        public static final String EMAIL_EXIST = "Email already exist";
        public static final String NOT_FOUND_HANDLE = "Not found api";
        public static final String NOT_ALLOWED = "Method not allowed";
        public static final String ACCOUNT_DEACTIVE = "Account is deactive";
        public static final String ROLE_ERROR = "Role name is not exist";
    }

    public static final class ROLE {
        public static final String USER = "ROLE_USER";
        public static final String STAFF = "ROLE_STAFF";
        public static final String ADMIN = "ROLE_ADMIN";
    }

}
