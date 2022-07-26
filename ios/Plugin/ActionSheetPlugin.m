#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(ActionSheetPlugin, "ActionSheet",
        CAP_PLUGIN_METHOD(showActions, CAPPluginReturnPromise);
        CAP_PLUGIN_METHOD(cancel, CAPPluginReturnPromise);
)
