# __MVP__ (__Model__-__View__-__Presenter__)

___

Mô hình __MVP__ cũng gần giống với mô hình __MVC__. Nó được kế thừa từ mô hình __MVC__, trong đó __Controller__ được thay thế bới __Presenter__. Mô hình này chia ứng dụng thành 3 phần chính: __Model__, __View__ và __Presenter__.

1. __Model__ đại diện cho một tập hợp các lớp mô tả business logic (business model and the data model). Nó cũng định nghĩa các business rules cho dữ liệu (nghĩa là cách mà dữ liệu thay đổi và được dùng)

2. __View__ là thành phần tương tác trực tiếp với người dùng như XML, Activity, Fragments. Nó không bao gồm bất kỳ việc xử lý logic nào.

3. __Presenter__ sẽ nhận input của người dùng thông qua __View__, rồi xử lý dữ liệu của người dùng với sự trợ giúp của __Model__ và trả kết quả về __View__. __Presenter__ giao tiếp với __View__ qua interface. Interface được định nghĩa trong lớp __Presenter__(với cái nó cần truyền dữ liệu). Activity\/Fragment hoặc các __View__ component khác implement interface này và render dữ liệu.

Trong cấu trúc __MVP__, __Presenter__ thao túng __Model__ và cập nhật ở __View__. __View__ và Presenter tách biệt với nhau hoàn toàn và giao tiếp với nhau qua thông qua interface. Vì nếu tách riêng từng phần ở __View__ sẽ dễ dàng cho việc kiểm thử ứng dụng ở __MVP__ hơn so với mô hình __MVC__.

___

## LOGIN APP VỚI MVP

- ta sẽ thực hiện thiết kế 1 chương trình có giao diện như sau theo mô hình kiến trúc MVC

<img src="https://github.com/hienqp/Ngay047-ArchitecturePattern-MVCTutorial/blob/main/UI_SAMPLE.png">

- với ứng dụng như hình trên, ta có hoạt động của ứng dụng như sau
	- user nhập email, password, sau đó click Login
	- chương trình sẽ xử lý với dữ liệu user nhập vào
	- hiển thị message Login thành công hay thất bại cho user biết

___

## __MODEL__

- __Model__ chính là Instance của Object có các thuộc tính __email__ và __password__
- __Model__ là nơi chứa data, xử lý data nhận được từ Presenter
- ta sẽ xây dựng 1 class Model có tên là User

- __User.java__
```java
public class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // kiểm tra chuỗi email có hợp lệ hay không
    public boolean isValidEmail() {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // kiểm tra chuỗi password có hợp lệ hay không
    public boolean isValidPassword() {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }
}
```

___

## __VIEW__

- với View chính là những thành phần hiển thị cho user nhìn thấy và tương tác
- trong mô hình __MVP__ thì __View__ bao gồm bộ đôi layout và file logic của layout (activity_main.xml và MainActivity.java)
- thiết kế Layout View __activity_main.xml__ như sau

- __activity_main.xml__
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center_horizontal"
    android:orientation="vertical"
    android:padding="16dp"
    tools:context=".MainActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/login"
        android:textColor="@color/black"
        android:textSize="30sp" />

    <EditText
        android:id="@+id/edt_email"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="30dp"
        android:autofillHints="@string/email"
        android:hint="@string/email"
        android:inputType="textEmailAddress"
        android:textColor="@color/black"
        android:textSize="20sp" />

    <EditText
        android:id="@+id/edt_password"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="15dp"
        android:autofillHints="@string/password"
        android:hint="@string/password"
        android:inputType="numberPassword"
        android:textColor="@color/black"
        android:textSize="20sp" />

    <TextView
        android:id="@+id/tv_message"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="30dp"
        android:text="@string/successful_message_here"
        android:textSize="18sp"
        android:visibility="gone" />

    <Button
        android:id="@+id/btn_login"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="30dp"
        android:text="@string/login"
        android:textSize="20sp" />

</LinearLayout>
```

- file xử lý logic của layout tạm xây dựng như sau
- __MainActivity.java__
```java
public class MainActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private TextView tvMessage;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        btnLogin = (Button) findViewById(R.id.btn_login);

        // lắng nghe sự kiện user click vào Button Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            // nếu user click vào Button Login, method clickLogin() sẽ được gọi
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });
    }

    private void clickLogin() {
        // 2 biến tạm dùng để lưu giá trị của 2 EditText
        String strEmail = edtEmail.getText().toString();
        String strPassword = edtPassword.getText().toString();

        // khởi tạo object model User
        User user = new User(strEmail, strPassword);
    }
```
___

## __BỘ ĐÔI PRESENTER VÀ INTERFACE CỦA PRESENTER__

- trong mô hình MVP thì Presenter là thành phần duy nhất có nhiệm vụ đảm nhận xử lý tất cả các logic nghiệp vụ giữa View và Model (trừ logic riêng của Model hoặc riêng của thành phần khác như View)
- Presenter sẽ là trung gian giữa View và Model, và trong Presenter không được khai báo hay gọi trực tiếp đến View, vậy để liên lạc được với View, phải sử dụng Interface
- ta sẽ xây dựng 1 class LoginPresenter

- __LoginPresenter.java__
```java
public class LoginPresenter {
	//
}
```

- ta sẽ xây dựng LoginInterface để làm callback, khi LoginPresenter xử lý logic với Model, sẽ callback ngược lại View để trả về dữ liệu đã xử lý với Model, để View có thể hiển thị thông tin tương ứng cho user
- khi user click button login, LoginPresenter sẽ hỏi model User những dữ liệu lấy từ View có đúng hay không (nghĩa là login success hay error), với kết quả trả về tương ứng từ model User, Presenter sẽ callback ngược lại View, và View sẽ hiển thị tương ứng với thông tin mà Presenter đã thông báo
- như vậy View sẽ implements LoginInterface, LoginInterface sẽ là 1 thành phần của Presenter

- __LoginInterface.java__
```java
public interface LoginInterface {
    void loginSuccess();
    void loginError();
}
```

- khai báo LoginInterface trong LoginPresenter
```java
public class LoginPresenter {
    private LoginInterface mLoginInterface;

    public LoginPresenter(LoginInterface mLoginInterface) {
        this.mLoginInterface = mLoginInterface;
    }
}
```

- xử lý logic với model từ dữ liệu nhận được từ View, mà dữ liệu nhận được từ view là dữ liệu thuộc về User
```java
public class LoginPresenter {
    private LoginInterface mLoginInterface;

    public LoginPresenter(LoginInterface mLoginInterface) {
        this.mLoginInterface = mLoginInterface;
    }

    public void login(User user) {
        if (user.isValidEmail() && user.isValidPassword()) {
            mLoginInterface.loginSuccess();
        } else {
            mLoginInterface.loginError();
        }
    }
}
```

- đến lúc này thì Presenter vẫn chưa gọi được cho View, mà chỉ là hỏi Model, sau đó gọi cho Interface thực thi method của Interface
- để liên kết được Presenter đến View, thì View phải implements Interaface của Presenter

___

## SỬ DỤNG INTERFACE LÀM MÓC DỮ LIỆU GIỮA VIEW VÀ PRESENTER

- MainActivity sẽ implements LoginInterface và override lại 2 method của interface này
```java
public class MainActivity extends AppCompatActivity implements LoginInterface{
    private EditText edtEmail, edtPassword;
    private TextView tvMessage;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        btnLogin = (Button) findViewById(R.id.btn_login);

        // lắng nghe sự kiện user click vào Button Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            // nếu user click vào Button Login, method clickLogin() sẽ được gọi
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });
    }

    private void clickLogin() {
        // 2 biến tạm dùng để lưu giá trị của 2 EditText
        String strEmail = edtEmail.getText().toString();
        String strPassword = edtPassword.getText().toString();

        // khởi tạo object model User
        User user = new User(strEmail, strPassword);
    }

    @Override
    public void loginSuccess() {
        tvMessage.setText("Login Success");
        tvMessage.setTextColor(getResources().getColor(R.color.purple_200, null));
        tvMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void loginError() {
        tvMessage.setText("Email or Password Invalid");
        tvMessage.setTextColor(getResources().getColor(R.color.purple_700, null));
        tvMessage.setVisibility(View.VISIBLE);
    }
}
```

- lúc này giữa View và Presenter vẫn chưa có sự liên kết, ta cần
	- khai báo LoginPresenter
	- khởi tạo LoginPresenter với tham số truyền vào chính là MainActivity đã implements LoginInterface (lúc này MainActivity là Instance của LoginInterface)
	- khi người dùng click button LOGIN thì view MainActivity sẽ gọi cho presenter LoginPresenter rằng vừa nhận được data của model User (tức là yêu cầu presenter xử lý)
	- sau khi ở presenter LoginPresenter xử lý với model User sẽ gọi method tương ứng với thể hiện của LoginInterface (lúc này là view MainActivity) tức là gọi đến method tương ứng của view MainActivity đã override

```java
public class MainActivity extends AppCompatActivity implements LoginInterface{
    private EditText edtEmail, edtPassword;
    private TextView tvMessage;
    private Button btnLogin;

    // khai báo presenter LoginPresenter để liên lạc
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        btnLogin = (Button) findViewById(R.id.btn_login);

        // khởi tạo presenter LoginPresenter với instance của chính view MainActivity đã implements interface LoginInterface
        mLoginPresenter = new LoginPresenter(this);

        // lắng nghe sự kiện user click vào Button Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            // nếu user click vào Button Login, method clickLogin() sẽ được gọi
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });
    }

    private void clickLogin() {
        // 2 biến tạm dùng để lưu giá trị của 2 EditText
        String strEmail = edtEmail.getText().toString();
        String strPassword = edtPassword.getText().toString();

        // khởi tạo object model User
        User user = new User(strEmail, strPassword);

        // view MainActivity truyền dữ liệu cho presenter LoginPresenter
        mLoginPresenter.login(user);
    }

    @Override
    public void loginSuccess() {
        tvMessage.setText("Login Success");
        tvMessage.setTextColor(getResources().getColor(R.color.purple_200, null));
        tvMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void loginError() {
        tvMessage.setText("Email or Password Invalid");
        tvMessage.setTextColor(getResources().getColor(R.color.purple_700, null));
        tvMessage.setVisibility(View.VISIBLE);
    }
}
```

___

- trong Tutorial MVP này
	- View (activity_main.xml và MainActivity.java): có nhiệm vụ thu thập dữ liệu của model, gọi và truyền dữ liệu model cho presenter, sau đó chờ presenter callback ngược lại thông qua interface đã implements
	- Presenter (LoginPresenter): nhận liên kết với view thông qua interface, khi view implements LoginInterface, đến khi khởi tạo presenter trong view với việc truyền instance của view chính là interface (do đã implements) coi như view đã liên kết đến presenter, khi presenter gọi đến method trong interface LoginInterface chính là presenter trả lời kết quả đến view, và gọi view thực hiện kết quả trả về
	- Model (User.java): có nhiệm vụ lưu trữ dữ liệu của 1 đối tượng nào đó mà ta đang tác động (các thuộc tính) và xử lý logic chỉ trên các dữ liệu đó
	- Interface(LoginInterface): có nhiệm vụ làm cầu nối giữa presenter và view, thông qua việc view sẽ implements và override các method, còn presenter callback các method đó cho view

END