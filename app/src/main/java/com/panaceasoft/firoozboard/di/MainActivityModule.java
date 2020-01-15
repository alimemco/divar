package com.panaceasoft.firoozboard.di;


import com.panaceasoft.firoozboard.MainActivity;
import com.panaceasoft.firoozboard.ui.apploading.AppLoadingActivity;
import com.panaceasoft.firoozboard.ui.apploading.AppLoadingFragment;
import com.panaceasoft.firoozboard.ui.blog.detail.BlogDetailActivity;
import com.panaceasoft.firoozboard.ui.blog.detail.BlogDetailFragment;
import com.panaceasoft.firoozboard.ui.blog.list.BlogListActivity;
import com.panaceasoft.firoozboard.ui.blog.list.BlogListFragment;
import com.panaceasoft.firoozboard.ui.category.categoryfilter.CategoryFilterFragment;
import com.panaceasoft.firoozboard.ui.category.list.CategoryListActivity;
import com.panaceasoft.firoozboard.ui.category.list.CategoryListFragment;
import com.panaceasoft.firoozboard.ui.chat.chat.ChatActivity;
import com.panaceasoft.firoozboard.ui.chat.chat.ChatFragment;
import com.panaceasoft.firoozboard.ui.chat.chat.ChatNotFragment;
import com.panaceasoft.firoozboard.ui.chat.chatimage.ChatImageFullScreenActivity;
import com.panaceasoft.firoozboard.ui.chat.chatimage.ChatImageFullScreenFragment;
import com.panaceasoft.firoozboard.ui.chathistory.BuyerFragment;
import com.panaceasoft.firoozboard.ui.chathistory.MessageFragment;
import com.panaceasoft.firoozboard.ui.chathistory.SellerFragment;
import com.panaceasoft.firoozboard.ui.city.menu.CityMenuFragment;
import com.panaceasoft.firoozboard.ui.city.selectedcity.SelectedCityActivity;
import com.panaceasoft.firoozboard.ui.city.selectedcity.SelectedCityFragment;
import com.panaceasoft.firoozboard.ui.comment.detail.CommentDetailActivity;
import com.panaceasoft.firoozboard.ui.comment.detail.CommentDetailFragment;
import com.panaceasoft.firoozboard.ui.comment.list.CommentListActivity;
import com.panaceasoft.firoozboard.ui.comment.list.CommentListFragment;
import com.panaceasoft.firoozboard.ui.contactus.ContactUsActivity;
import com.panaceasoft.firoozboard.ui.contactus.ContactUsFragment;
import com.panaceasoft.firoozboard.ui.customcamera.CameraActivity;
import com.panaceasoft.firoozboard.ui.customcamera.CameraFragment;
import com.panaceasoft.firoozboard.ui.customcamera.setting.CameraSettingActivity;
import com.panaceasoft.firoozboard.ui.customcamera.setting.CameraSettingFragment;
import com.panaceasoft.firoozboard.ui.dashboard.DashBoardSearchActivity;
import com.panaceasoft.firoozboard.ui.dashboard.DashBoardSearchCategoryFragment;
import com.panaceasoft.firoozboard.ui.dashboard.DashBoardSearchFragment;
import com.panaceasoft.firoozboard.ui.dashboard.DashBoardSearchSubCategoryFragment;
import com.panaceasoft.firoozboard.ui.dashboard.DashboardSearchByCategoryActivity;
import com.panaceasoft.firoozboard.ui.forceupdate.ForceUpdateActivity;
import com.panaceasoft.firoozboard.ui.forceupdate.ForceUpdateFragment;
import com.panaceasoft.firoozboard.ui.gallery.GalleryActivity;
import com.panaceasoft.firoozboard.ui.gallery.GalleryFragment;
import com.panaceasoft.firoozboard.ui.gallery.detail.GalleryDetailActivity;
import com.panaceasoft.firoozboard.ui.gallery.detail.GalleryDetailFragment;
import com.panaceasoft.firoozboard.ui.item.detail.ItemActivity;
import com.panaceasoft.firoozboard.ui.item.detail.ItemFragment;
import com.panaceasoft.firoozboard.ui.item.entry.ItemEntryActivity;
import com.panaceasoft.firoozboard.ui.item.entry.ItemEntryFragment;
import com.panaceasoft.firoozboard.ui.item.favourite.FavouriteListActivity;
import com.panaceasoft.firoozboard.ui.item.favourite.FavouriteListFragment;
import com.panaceasoft.firoozboard.ui.item.history.HistoryFragment;
import com.panaceasoft.firoozboard.ui.item.history.UserHistoryListActivity;
import com.panaceasoft.firoozboard.ui.item.itemcondition.ItemConditionFragment;
import com.panaceasoft.firoozboard.ui.item.itemdealoption.ItemDealOptionTypeFragment;
import com.panaceasoft.firoozboard.ui.item.itemfromfollower.ItemFromFollowerListActivity;
import com.panaceasoft.firoozboard.ui.item.itemfromfollower.ItemFromFollowerListFragment;
import com.panaceasoft.firoozboard.ui.item.itemlocation.ItemLocationFragment;
import com.panaceasoft.firoozboard.ui.item.itempricetype.ItemPriceTypeFragment;
import com.panaceasoft.firoozboard.ui.item.itemtype.ItemTypeFragment;
import com.panaceasoft.firoozboard.ui.item.itemtype.SearchViewActivity;
import com.panaceasoft.firoozboard.ui.item.loginUserItem.LoginUserItemFragment;
import com.panaceasoft.firoozboard.ui.item.loginUserItem.LoginUserItemListActivity;
import com.panaceasoft.firoozboard.ui.item.map.MapActivity;
import com.panaceasoft.firoozboard.ui.item.map.MapFragment;
import com.panaceasoft.firoozboard.ui.item.map.PickMapFragment;
import com.panaceasoft.firoozboard.ui.item.map.mapFilter.MapFilteringActivity;
import com.panaceasoft.firoozboard.ui.item.map.mapFilter.MapFilteringFragment;
import com.panaceasoft.firoozboard.ui.item.myitem.MyItemFragment;
import com.panaceasoft.firoozboard.ui.item.myitem.MyItemListActivity;
import com.panaceasoft.firoozboard.ui.item.rating.RatingListActivity;
import com.panaceasoft.firoozboard.ui.item.rating.RatingListFragment;
import com.panaceasoft.firoozboard.ui.item.readmore.ReadMoreActivity;
import com.panaceasoft.firoozboard.ui.item.readmore.ReadMoreFragment;
import com.panaceasoft.firoozboard.ui.item.search.searchlist.SearchListActivity;
import com.panaceasoft.firoozboard.ui.item.search.searchlist.SearchListFragment;
import com.panaceasoft.firoozboard.ui.item.search.specialfilterbyattributes.FilteringActivity;
import com.panaceasoft.firoozboard.ui.item.search.specialfilterbyattributes.FilteringFragment;
import com.panaceasoft.firoozboard.ui.language.LanguageFragment;
import com.panaceasoft.firoozboard.ui.location.LocationActivity;
import com.panaceasoft.firoozboard.ui.notification.detail.NotificationActivity;
import com.panaceasoft.firoozboard.ui.notification.detail.NotificationFragment;
import com.panaceasoft.firoozboard.ui.notification.list.NotificationListActivity;
import com.panaceasoft.firoozboard.ui.notification.list.NotificationListFragment;
import com.panaceasoft.firoozboard.ui.notification.setting.NotificationSettingActivity;
import com.panaceasoft.firoozboard.ui.notification.setting.NotificationSettingFragment;
import com.panaceasoft.firoozboard.ui.setting.SettingActivity;
import com.panaceasoft.firoozboard.ui.setting.SettingFragment;
import com.panaceasoft.firoozboard.ui.setting.appinfo.AppInfoActivity;
import com.panaceasoft.firoozboard.ui.setting.appinfo.AppInfoFragment;
import com.panaceasoft.firoozboard.ui.subcategory.SubCategoryActivity;
import com.panaceasoft.firoozboard.ui.subcategory.SubCategoryFragment;
import com.panaceasoft.firoozboard.ui.terms.TermsActivity;
import com.panaceasoft.firoozboard.ui.terms.TermsFragment;
import com.panaceasoft.firoozboard.ui.user.PasswordChangeActivity;
import com.panaceasoft.firoozboard.ui.user.PasswordChangeFragment;
import com.panaceasoft.firoozboard.ui.user.ProfileEditActivity;
import com.panaceasoft.firoozboard.ui.user.ProfileEditFragment;
import com.panaceasoft.firoozboard.ui.user.ProfileFragment;
import com.panaceasoft.firoozboard.ui.user.ProfileNotFragment;
import com.panaceasoft.firoozboard.ui.user.UserFBRegisterActivity;
import com.panaceasoft.firoozboard.ui.user.UserFBRegisterFragment;
import com.panaceasoft.firoozboard.ui.user.UserForgotPasswordActivity;
import com.panaceasoft.firoozboard.ui.user.UserForgotPasswordFragment;
import com.panaceasoft.firoozboard.ui.user.UserLoginActivity;
import com.panaceasoft.firoozboard.ui.user.UserLoginFragment;
import com.panaceasoft.firoozboard.ui.user.UserRegisterActivity;
import com.panaceasoft.firoozboard.ui.user.UserRegisterFragment;
import com.panaceasoft.firoozboard.ui.user.userlist.UserListActivity;
import com.panaceasoft.firoozboard.ui.user.userlist.UserListFragment;
import com.panaceasoft.firoozboard.ui.user.userlist.detail.UserDetailActivity;
import com.panaceasoft.firoozboard.ui.user.userlist.detail.UserDetailFragment;
import com.panaceasoft.firoozboard.ui.user.verifyemail.VerifyEmailActivity;
import com.panaceasoft.firoozboard.ui.user.verifyemail.VerifyEmailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

//import com.panaceasoft.firoozboard.ui.followinguser.FollowingUserActivity;
//import com.panaceasoft.firoozboard.ui.followinguser.FollowingUserFragment;
//import com.panaceasoft.firoozboard.ui.followinguser.detail.FollowingUserDetailActivity;
//import com.panaceasoft.firoozboard.ui.followinguser.detail.FollowingUserDetailFragment;

/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FavouriteListModule.class)
    abstract FavouriteListActivity contributeFavouriteListActivity();

    @ContributesAndroidInjector(modules = UserHistoryModule.class)
    abstract UserHistoryListActivity contributeUserHistoryListActivity();

    @ContributesAndroidInjector(modules = UserRegisterModule.class)
    abstract UserRegisterActivity contributeUserRegisterActivity();

    @ContributesAndroidInjector(modules = UserFBRegisterModule.class)
    abstract UserFBRegisterActivity contributeUserFBRegisterActivity();

    @ContributesAndroidInjector(modules = UserForgotPasswordModule.class)
    abstract UserForgotPasswordActivity contributeUserForgotPasswordActivity();

    @ContributesAndroidInjector(modules = ContactUsModule.class)
    abstract ContactUsActivity contributeContactUsActivity();

    @ContributesAndroidInjector(modules = UserLoginModule.class)
    abstract UserLoginActivity contributeUserLoginActivity();

    @ContributesAndroidInjector(modules = PasswordChangeModule.class)
    abstract PasswordChangeActivity contributePasswordChangeActivity();

    @ContributesAndroidInjector(modules = FilteringModule.class)
    abstract FilteringActivity filteringActivity();

    @ContributesAndroidInjector(modules = SubCategoryActivityModule.class)
    abstract SubCategoryActivity subCategoryActivity();

    @ContributesAndroidInjector(modules = CommentDetailModule.class)
    abstract CommentDetailActivity commentDetailActivity();

    @ContributesAndroidInjector(modules = NotificationModule.class)
    abstract NotificationListActivity notificationActivity();

    @ContributesAndroidInjector(modules = CameraSettingActivityModule.class)
    abstract CameraSettingActivity cameraSettingActivity();

    @ContributesAndroidInjector(modules = TermsActivityModule.class)
    abstract TermsActivity termsActivity();

    @ContributesAndroidInjector(modules = SearchActivityModule.class)
    abstract SearchListActivity contributeSearchListActivity();

    @ContributesAndroidInjector(modules = CameraActivityModule.class)
    abstract CameraActivity contributeCameraActivity();

    @ContributesAndroidInjector(modules = ItemEntryActivityModule.class)
    abstract ItemEntryActivity contributeItemEntryActivity();

    @ContributesAndroidInjector(modules = NotificationDetailModule.class)
    abstract NotificationActivity notificationDetailActivity();

    @ContributesAndroidInjector(modules = ItemActivityModule.class)
    abstract ItemActivity itemActivity();

    @ContributesAndroidInjector(modules = MyItemActivityModule.class)
    abstract MyItemListActivity myitemActivity();

    @ContributesAndroidInjector(modules = CommentListActivityModule.class)
    abstract CommentListActivity commentListActivity();

    @ContributesAndroidInjector(modules = GalleryDetailActivityModule.class)
    abstract GalleryDetailActivity galleryDetailActivity();

    @ContributesAndroidInjector(modules = GalleryActivityModule.class)
    abstract GalleryActivity galleryActivity();

    @ContributesAndroidInjector(modules = SearchByCategoryActivityModule.class)
    abstract DashboardSearchByCategoryActivity searchByCategoryActivity();

    @ContributesAndroidInjector(modules = readMoreActivityModule.class)
    abstract ReadMoreActivity readMoreActivity();

    @ContributesAndroidInjector(modules = EditSettingModule.class)
    abstract SettingActivity editSettingActivity();

    @ContributesAndroidInjector(modules = LanguageChangeModule.class)
    abstract NotificationSettingActivity languageChangeActivity();

    @ContributesAndroidInjector(modules = ProfileEditModule.class)
    abstract ProfileEditActivity contributeProfileEditActivity();

    @ContributesAndroidInjector(modules = AppInfoModule.class)
    abstract AppInfoActivity AppInfoActivity();

    @ContributesAndroidInjector(modules = CategoryListActivityAppInfoModule.class)
    abstract CategoryListActivity categoryListActivity();

    @ContributesAndroidInjector(modules = RatingListActivityModule.class)
    abstract RatingListActivity ratingListActivity();

    @ContributesAndroidInjector(modules = SelectedCityModule.class)
    abstract SelectedCityActivity selectedShopActivity();

    @ContributesAndroidInjector(modules = SelectedShopListBlogModule.class)
    abstract BlogListActivity selectedShopListBlogActivity();

    @ContributesAndroidInjector(modules = BlogDetailModule.class)
    abstract BlogDetailActivity blogDetailActivity();

    @ContributesAndroidInjector(modules = MapActivityModule.class)
    abstract MapActivity mapActivity();

    @ContributesAndroidInjector(modules = forceUpdateModule.class)
    abstract ForceUpdateActivity forceUpdateActivity();

    @ContributesAndroidInjector(modules = MapFilteringModule.class)
    abstract MapFilteringActivity mapFilteringActivity();

    @ContributesAndroidInjector(modules = SearchViewActivityModule.class)
    abstract SearchViewActivity searchViewActivity();

    @ContributesAndroidInjector(modules = chatActivityModule.class)
    abstract ChatActivity chatActivity();

    @ContributesAndroidInjector(modules = ImageFullScreenModule.class)
    abstract ChatImageFullScreenActivity imageFullScreenActivity();

    @ContributesAndroidInjector(modules = DashBoardSearchModule.class)
    abstract DashBoardSearchActivity dashBoardSearchActivity();

    @ContributesAndroidInjector(modules = LoginUserItemModule.class)
    abstract LoginUserItemListActivity contributeLoginUserItemListActivity();

    @ContributesAndroidInjector(modules = FollowerUserModule.class)
    abstract UserListActivity contributeFollowerUserListActivity();

    @ContributesAndroidInjector(modules = VerifyEmailModule.class)
    abstract VerifyEmailActivity contributeVerifyEmailActivity();

    @ContributesAndroidInjector(modules = FollowerUserDetailModule.class)
    abstract UserDetailActivity contributeFollowerUserDetailActivity();

    @ContributesAndroidInjector(modules = AppLoadingActivityModule.class)
    abstract AppLoadingActivity appLoadingActivity();

    @ContributesAndroidInjector(modules = ItemFromFollowerListModule.class)
    abstract ItemFromFollowerListActivity itemFromFollowerListActivity();

    @ContributesAndroidInjector(modules = LocationActivityModule.class)
    abstract LocationActivity locationActivity();

}


@Module
abstract class MainModule {

    @ContributesAndroidInjector
    abstract ContactUsFragment contributeContactUsFragment();

    @ContributesAndroidInjector
    abstract BuyerFragment contributeBuyerFragment();

    @ContributesAndroidInjector
    abstract SellerFragment contributeSellerFragment();

    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();

    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();

    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();

    @ContributesAndroidInjector
    abstract UserFBRegisterFragment contributeUserFBRegisterFragment();

    @ContributesAndroidInjector
    abstract NotificationSettingFragment contributeNotificationSettingFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract ProfileNotFragment contributeProfileNotFragment();

    @ContributesAndroidInjector
    abstract ChatNotFragment contributeChatNotFragment();

    @ContributesAndroidInjector
    abstract LanguageFragment contributeLanguageFragment();

    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteListFragment();

    @ContributesAndroidInjector
    abstract SettingFragment contributEditSettingFragment();

    @ContributesAndroidInjector
    abstract HistoryFragment historyFragment();

    @ContributesAndroidInjector
    abstract NotificationListFragment contributeNotificationFragment();

    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();

    @ContributesAndroidInjector
    abstract SelectedCityFragment contributeSelectedCityFragment();

    @ContributesAndroidInjector
    abstract SearchListFragment contributeSearchListFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryListFragment();

    @ContributesAndroidInjector
    abstract MessageFragment contributeMessageFragment();


    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();
}


@Module
abstract class ProfileEditModule {
    @ContributesAndroidInjector
    abstract ProfileEditFragment contributeProfileEditFragment();
}

@Module
abstract class UserFBRegisterModule {
    @ContributesAndroidInjector
    abstract UserFBRegisterFragment contributeUserFBRegisterFragment();
}

@Module
abstract class ItemActivityModule {
    @ContributesAndroidInjector
    abstract ItemFragment contributeItemFragment();
}
@Module
abstract class MyItemActivityModule {
    @ContributesAndroidInjector
    abstract MyItemFragment contributeMyItemFragment();
}

@Module
abstract class FavouriteListModule {
    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteFragment();
}


@Module
abstract class UserRegisterModule {
    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();
}

@Module
abstract class UserForgotPasswordModule {
    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();
}

@Module
abstract class ContactUsModule {
    @ContributesAndroidInjector
    abstract ContactUsFragment contributeUserForgotPasswordFragment();
}
@Module
abstract class UserLoginModule {
    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();
}

@Module
abstract class PasswordChangeModule {
    @ContributesAndroidInjector
    abstract PasswordChangeFragment contributePasswordChangeFragment();
}

@Module
abstract class CommentDetailModule {
    @ContributesAndroidInjector
    abstract CommentDetailFragment commentDetailFragment();
}


@Module
abstract class NotificationModule {
    @ContributesAndroidInjector
    abstract NotificationListFragment notificationFragment();
}

@Module
abstract class CameraSettingActivityModule {
    @ContributesAndroidInjector
    abstract CameraSettingFragment cameraSettingFragment();
}

@Module
abstract class TermsActivityModule {
    @ContributesAndroidInjector
    abstract TermsFragment termsFragment();
}

@Module
abstract class NotificationDetailModule {
    @ContributesAndroidInjector
    abstract NotificationFragment notificationDetailFragment();
}

@Module
abstract class UserHistoryModule {
    @ContributesAndroidInjector
    abstract HistoryFragment contributeHistoryFragment();
}

@Module
abstract class AppInfoModule {
    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();
}

@Module
abstract class CategoryListActivityAppInfoModule {
    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

}

@Module
abstract class RatingListActivityModule {
    @ContributesAndroidInjector
    abstract RatingListFragment contributeRatingListFragment();
}

@Module
abstract class readMoreActivityModule {
    @ContributesAndroidInjector
    abstract ReadMoreFragment contributeReadMoreFragment();
}

@Module
abstract class EditSettingModule {
    @ContributesAndroidInjector
    abstract SettingFragment EditSettingFragment();
}

@Module
abstract class LanguageChangeModule {
    @ContributesAndroidInjector
    abstract NotificationSettingFragment notificationSettingFragment();
}

@Module
abstract class EditProfileModule {
    @ContributesAndroidInjector
    abstract ProfileFragment ProfileFragment();
}

@Module
abstract class EditProfileNotModule {
    @ContributesAndroidInjector
    abstract ProfileNotFragment ProfileNotFragment();
}

@Module
abstract class EditChatNotModule {
    @ContributesAndroidInjector
    abstract ChatNotFragment ChatNotFragment();
}

@Module
abstract class SubCategoryActivityModule {
    @ContributesAndroidInjector
    abstract SubCategoryFragment contributeSubCategoryFragment();

}

@Module
abstract class FilteringModule {

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract FilteringFragment contributeSpecialFilteringFragment();

}

@Module
abstract class SearchActivityModule {
    @ContributesAndroidInjector
    abstract SearchListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

}

@Module
abstract class CommentListActivityModule {
    @ContributesAndroidInjector
    abstract CommentListFragment contributeCommentListFragment();
}

@Module
abstract class CameraActivityModule {
    @ContributesAndroidInjector
    abstract CameraFragment contributeCameraFragment();
}

@Module
abstract class ItemEntryActivityModule {
    @ContributesAndroidInjector
    abstract ItemEntryFragment contributeItemEntryFragment();
}

@Module
abstract class GalleryDetailActivityModule {
    @ContributesAndroidInjector
    abstract GalleryDetailFragment contributeGalleryDetailFragment();
}

@Module
abstract class GalleryActivityModule {
    @ContributesAndroidInjector
    abstract GalleryFragment contributeGalleryFragment();
}

@Module
abstract class SearchByCategoryActivityModule {

    @ContributesAndroidInjector
    abstract DashBoardSearchCategoryFragment contributeDashBoardSearchCategoryFragment();

    @ContributesAndroidInjector
    abstract DashBoardSearchSubCategoryFragment contributeDashBoardSearchSubCategoryFragment();
}

@Module
abstract class SelectedCityModule {

    @ContributesAndroidInjector
    abstract SearchListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment categoryListFragment();

    @ContributesAndroidInjector
    abstract SelectedCityFragment contributeSelectedCityFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract CityMenuFragment contributeCityMenuFragment();

}

@Module
abstract class SelectedShopListBlogModule {

    @ContributesAndroidInjector
    abstract BlogListFragment contributeSelectedShopListBlogFragment();

}

@Module
abstract class BlogDetailModule {

    @ContributesAndroidInjector
    abstract BlogDetailFragment contributeBlogDetailFragment();
}

@Module
abstract class MapActivityModule {

    @ContributesAndroidInjector
    abstract MapFragment contributeMapFragment();

    @ContributesAndroidInjector
    abstract PickMapFragment contributePickMapFragment();

}

@Module
abstract class forceUpdateModule {

    @ContributesAndroidInjector
    abstract ForceUpdateFragment contributeForceUpdateFragment();
}

@Module
abstract class MapFilteringModule {

    @ContributesAndroidInjector
    abstract MapFilteringFragment contributeMapFilteringFragment();
}

@Module
abstract class SearchViewActivityModule {


    @ContributesAndroidInjector
    abstract ItemConditionFragment contributeItemConditionFragment();

    @ContributesAndroidInjector
    abstract ItemLocationFragment contributeItemLocationFragment();

    @ContributesAndroidInjector
    abstract ItemDealOptionTypeFragment contributeItemDealOptionTypeFragment();

    @ContributesAndroidInjector
    abstract ItemPriceTypeFragment contributeItemPriceTypeFragment();

    @ContributesAndroidInjector
    abstract ItemTypeFragment contributeItemTypeFragment();


}

@Module
abstract class chatActivityModule {

    @ContributesAndroidInjector
    abstract ChatFragment contributeChatFragment();
}

@Module
abstract class ImageFullScreenModule {

    @ContributesAndroidInjector
    abstract ChatImageFullScreenFragment contributeImageFullScreenFragment();

}
@Module
abstract class DashBoardSearchModule {

    @ContributesAndroidInjector
    abstract DashBoardSearchFragment contributeDashBoardSearchFragment();

}

@Module
abstract class LoginUserItemModule {
    @ContributesAndroidInjector
    abstract LoginUserItemFragment contributeLoginUserItemFragment();
}

@Module
abstract class FollowerUserModule {
    @ContributesAndroidInjector
    abstract UserListFragment contributeFollowerUserFragment();
}

@Module
abstract class VerifyEmailModule {
    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();
}
@Module
abstract class FollowerUserDetailModule {
    @ContributesAndroidInjector
    abstract UserDetailFragment contributeFollowerUserDetailFragment();
}

@Module
abstract class AppLoadingActivityModule {

    @ContributesAndroidInjector
    abstract AppLoadingFragment contributeAppLoadingFragment();
}

@Module
abstract class ItemFromFollowerListModule {

    @ContributesAndroidInjector
    abstract ItemFromFollowerListFragment contributeItemFromFollowerListFragment();
}

@Module
abstract class LocationActivityModule {

    @ContributesAndroidInjector
    abstract ItemLocationFragment contributeItemLocationFragment();

}
