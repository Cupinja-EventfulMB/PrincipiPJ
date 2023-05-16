package data.di

import data.model.Location
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun  provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                Location::class, Event::class
            )
        )
            .compactOnLaunch()
            .build()
        return  Realm.open(config)
    }


}