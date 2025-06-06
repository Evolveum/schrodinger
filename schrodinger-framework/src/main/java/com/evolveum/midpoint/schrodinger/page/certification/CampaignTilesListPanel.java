/*
 * Copyright (c) 2025 Evolveum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.evolveum.midpoint.schrodinger.page.certification;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.evolveum.midpoint.schrodinger.component.common.TileListPanel;
import com.evolveum.midpoint.schrodinger.util.Schrodinger;
import org.jetbrains.annotations.NotNull;

import static com.codeborne.selenide.Selenide.$$x;

public class CampaignTilesListPanel<T> extends TileListPanel<T> {

    public CampaignTilesListPanel(T parent, SelenideElement parentElement) {
        super(parent, parentElement);
    }

    public CampaignTilesListPanel<T> selectCampaignTileByNumber(int tileNumber) {
        selectTileByNumber(tileNumber);
        return this;
    }

    protected SelenideElement getCampaignTileElement(@NotNull String campaignName) {
        selectTileByLabel(campaignName);
        return null;
    }


}
