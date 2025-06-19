package org.hbrs.se2.project.startupx.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import org.hbrs.se2.project.startupx.dtos.UserDTO;
import org.hbrs.se2.project.startupx.util.Globals;
import org.hbrs.se2.project.startupx.views.investor.InvestorProfileView;
import org.hbrs.se2.project.startupx.views.investor.InvestorRegistrationView;
import org.hbrs.se2.project.startupx.views.student.StudentProfileView;
import org.hbrs.se2.project.startupx.views.student.StudentRegistrationView;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport("./styles/views/main/main-view.css")
@CssImport(value = "./styles/views/main/dark-mode.css", themeFor = "vaadin-app-layout")
@JsModule("./styles/shared-styles.js")
public class AppView extends AppLayout implements BeforeEnterObserver {

    private Tabs menu;
    private H1 viewTitle;
    private H1 helloUser;
    private MenuItem profileButton;

    public AppView() {
        if(getCurrentUser() != null) {
            setUpUI();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Class<? extends Component> target = (Class<? extends Component>) beforeEnterEvent.getNavigationTarget();

        // Falls der User nicht eingeloggt ist UND NICHT zur RegistrationView oder StudentRegistrationView navigiert wird
        if (this.getCurrentUser() == null &&
                !target.equals(InvestorRegistrationView.class) &&
                !target.equals(StudentRegistrationView.class)) {

            beforeEnterEvent.rerouteTo(MainView.class);
        }
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Falls der Benutzer nicht eingeloggt ist, dann wird er auf die Startseite gelenkt
        if ( !checkIfUserIsLoggedIn() ) return;

        // Der aktuell-selektierte Tab wird gehighlighted.
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);

        // Setzen des aktuellen Names des Tabs
        viewTitle.setText(getCurrentPageTitle());

        // Setzen des Vornamens von dem aktuell eingeloggten Benutzer
        helloUser.setText("Hallo! "  + this.getCurrentUser().getVorname() );

        //Ändert den Button von Profil nach Profil bearbeiten, wenn man sich auf dem eigenen Profil befindet
        Class<?> currentView = getContent().getClass();
        UserDTO currentUser = getCurrentUser();
        if (currentView.equals(StudentProfileView.class)) {
            profileButton.setText("Profil bearbeiten");
            profileButton.getElement().addEventListener("click", e -> switchToEditProfile());
        } else {
            profileButton.setText("Profil");
            profileButton.getElement().addEventListener("click", e -> switchToProfile());
        }
    }

    public void setUpUI() {
        // Anzeige des Toggles über den Drawer
        setPrimarySection(Section.DRAWER);

        // Erstellung der horizontalen Statusleiste (Header)
        addToNavbar(true, createHeaderContent());

        // Erstellung der vertikalen Navigationsleiste (Drawer)
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    /**
     * Erzeugung der horizontalen Leiste (Header).
     * @return
     */
    private Component createHeaderContent() {
        // Ein paar Grund-Einstellungen. Alles wird in ein horizontales Layout gesteckt.
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode( FlexComponent.JustifyContentMode.EVENLY );

        // Hinzufügen des Toogle ('Big Mac') zum Ein- und Ausschalten des Drawers
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        viewTitle.setWidthFull();
        layout.add( viewTitle );

        // Interner Layout
        HorizontalLayout topRightPanel = new HorizontalLayout();
        topRightPanel.setWidthFull();
        topRightPanel.setJustifyContentMode( FlexComponent.JustifyContentMode.END );
        topRightPanel.setAlignItems( FlexComponent.Alignment.CENTER );

        // Der Name des Users wird später reingesetzt, falls die Navigation stattfindet
        helloUser = new H1();
        topRightPanel.add(helloUser);

        MenuBar bar = new MenuBar();

        // Profil bearbeiten-Button am rechts-oberen Rand.
        profileButton = bar.addItem("Profil", e -> switchToProfile());

        // Logout-Button am rechts-oberen Rand.
        bar.addItem("Logout" , e -> logoutUser());
        topRightPanel.add(bar);

        //Test zum togglen zwischen Light und Darkmode
        Button toggleTheme = new Button("Dark Mode an/aus");
        toggleTheme.addClickListener(e -> {
            Element body = UI.getCurrent().getElement();
            if (body.getThemeList().contains("dark")) {
                body.getThemeList().remove("dark");
            } else {
                body.getThemeList().add("dark");
            }
        });

        topRightPanel.add(toggleTheme);

        layout.add( topRightPanel );
        return layout;
    }

    /**
     * Hinzufügen der vertikalen Leiste (Drawer)
     * Diese besteht aus dem Logo ganz oben links sowie den Menu-Einträgen (menu items).
     * Die Menu Items sind zudem verlinkt zu den internen Tab-Components.
     * @param menu
     * @return
     */
    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        HorizontalLayout logoLayout = new HorizontalLayout();

        // Hinzufügen des Logos
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "HelloCar logo"));
        logoLayout.add(new H1("StartUpX"));

        // Hinzufügen des Menus inklusive der Tabs
        layout.add(logoLayout, menu);
        return layout;
    }

    /**
     * Erzeugung des Menu auf der vertikalen Leiste (Drawer)
     * @return
     */
    private Tabs createMenu() {
        // Anlegen der Grundstruktur
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");

        // Anlegen der einzelnen Menuitems
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        return new Tab[]{ createTab( "Startseite", MainViewDashboard.class)
                , createTab( "Liste von StartUps", ShowAllStartUpsView.class)
                , createTab( "Jobbörse", JobListingView.class)
                , createTab("StartUp erstellen", CreateStartUpView.class)
        };
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private boolean checkIfUserIsLoggedIn() {
        // Falls der Benutzer nicht eingeloggt ist, dann wird er auf die Startseite gelenkt
        if (this.getCurrentUser() == null) {
            UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
            return false;
        }
        return true;
    }

    private void logoutUser() {
        UI ui = this.getUI().get();
        ui.getSession().close();
        ui.getPage().setLocation("/");
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    //Navigier zu ProfileView
    private void switchToProfile(){
        UserDTO currentUser = this.getCurrentUser();
        if(currentUser.getStudent() != null){
            UI.getCurrent().navigate(StudentProfileView.class);
        } else if (currentUser.getInvestor() != null){
            UI.getCurrent().navigate(InvestorProfileView.class);
        }
    }

    //Navigiere zu EditProfileView
    private void switchToEditProfile() {
        UI.getCurrent().navigate(EditProfileView.class);
    }

}
