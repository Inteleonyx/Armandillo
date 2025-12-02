package dev.inteleonyx.armandillo.tags;

import dev.inteleonyx.armandillo.api.luaj.LuaValue;
import dev.inteleonyx.armandillo.core.objects.ArmandilloModule;
import dev.inteleonyx.armandillo.tags.functions.*;

/**
 * @author Inteleonyx. Created on 02/12/2025
 * @project armandillo
 */

public class TagsArmandilloModule extends ArmandilloModule {
    LuaValue engine;

    public TagsArmandilloModule() {
        super("tags");
    }

    @Override
    public LuaValue getModuleEngine() {
        return this.engine;
    }

    @Override
    public void init() {
        this.engine.set("create_item_tag", new RemoveTagFunction());
        this.engine.set("create_block_tag", new AddBlockTagFunction());
        this.engine.set("remove", new RemoveTagFunction());
        this.engine.set("remove_all", new RemoveAllTagsFunction());
        this.engine.set("remove_all_tags_from", new RemoveAllTagsFromFunction());
    }
}
